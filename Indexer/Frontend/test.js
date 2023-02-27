// Search for public Tweets across the whole Twitter archive
// https://developer.twitter.com/en/docs/twitter-api/tweets/search/quick-start/full-archive-search

const needle = require('needle');
const {MongoClient} = require('mongodb');

const uri = "mongodb+srv://admin:admin1@clusty.gehnm.mongodb.net/database?retryWrites=true&w=majority";
const client = new MongoClient(uri);

// The code below sets the bearer token from your environment variables
// To set environment variables on macOS or Linux, run the export command below from the terminal:
// export BEARER_TOKEN='YOUR-TOKEN'

const token = 'AAAAAAAAAAAAAAAAAAAAAK6VbwEAAAAAyhjwqcNPGxIKkakiKilnUt706F8%3DoD8bxQ26rNpmg8Cfn1KP2yHlsSph4SK3B01H2K5F6Vy04onRtm';

const rulesURL = 'https://api.twitter.com/2/tweets/search/stream/rules';
const streamURL = 'https://api.twitter.com/2/tweets/search/stream?user.fields=username&tweet.fields=created_at,entities&place.fields=full_name,country,geo&expansions=author_id,geo.place_id';



// this sets up two rules - the value is the search terms to match on, and the tag is an identifier that
// will be applied to the Tweets return to show which rule they matched
// with a standard project with Basic Access, you can add up to 25 concurrent rules to your stream, and
// each rule can be up to 512 characters long

// Edit rules as desired below
const rules = [{
        'value': 'movies',
        'tag': 'movies'
    },
];

async function getAllRules() {
    await client.connect();

    const response = await needle('get', rulesURL, {
        headers: {
            "authorization": `Bearer ${token}`
        }
    })

    if (response.statusCode !== 200) {
        console.log("Error:", response.statusMessage, response.statusCode)
        throw new Error(response.body);
    }

    return (response.body);
}

async function insert(data) {
    const db = client.db('database')
    try {
        const json = JSON.parse(data);
        db.collection("Tweets").insertOne(json, function(err, res) {
            if (err) throw err;
            console.log("Document inserted");
            // close the connection to db when you are done with it
            // db.close();
        });
        console.log('populating');
        // A successful connection resets retry count.
        retryAttempt = 0;
    } catch (e) {
        console.log(e)
        if (data.detail === "This stream is currently at the maximum allowed connection limit.") {
            console.log(data.detail)
            process.exit(1)
        } else {
            // Keep alive signal received. Do nothing.
        }
    }
}

async function deleteAllRules(rules) {

    if (!Array.isArray(rules.data)) {
        return null;
    }

    const ids = rules.data.map(rule => rule.id);

    const data = {
        "delete": {
            "ids": ids
        }
    }

    const response = await needle('post', rulesURL, data, {
        headers: {
            "content-type": "application/json",
            "authorization": `Bearer ${token}`
        }
    })

    if (response.statusCode !== 200) {
        throw new Error(response.body);
    }

    return (response.body);

}

async function setRules() {

    const data = {
        "add": rules
    }

    const response = await needle('post', rulesURL, data, {
        headers: {
            "content-type": "application/json",
            "authorization": `Bearer ${token}`
        }
    })

    if (response.statusCode !== 201) {
        throw new Error(response.body);
    }

    return (response.body);

}

async function streamConnect(retryAttempt) {
    const tweets = [];
    const stream = needle.get(streamURL, {
        headers: {
            "User-Agent": "v2FilterStreamJS",
            "Authorization": `Bearer ${token}`
        },
        timeout: 20000
    });
    /*
    try {
        // Connect to the MongoDB cluster
        await client.connect();
 
        // Make the appropriate DB calls
        await  listDatabases(client);
 
    } catch (e) {
        console.error(e);
    } finally {
        await client.close();
    }
    */
    stream.on('data', data => {
        // if(tweets.length >= 2) {
            // console.log('FIRST TWEET!!!! \n', tweets[0]);
            // console.log('ENTITIES!!!!!!! \n', tweets[0].data.entities)
        // }
        insert(data);
    }).on('err', error => {
        if (error.code !== 'ECONNRESET') {
            console.log(error.code);
            process.exit(1);
        } else {
            // This reconnection logic will attempt to reconnect when a disconnection is detected.
            // To avoid rate limits, this logic implements exponential backoff, so the wait time
            // will increase if the client cannot reconnect to the stream. 
            setTimeout(() => {
                console.warn("A connection error occurred. Reconnecting...")
                streamConnect(++retryAttempt);
            }, 2 ** retryAttempt)
        }
    });

    return stream;

}


(async () => {
    let currentRules;

    try {
        // Gets the complete list of rules currently applied to the stream
        currentRules = await getAllRules();

        // Delete all rules. Comment the line below if you want to keep your existing rules.
        await deleteAllRules(currentRules);

        // Add rules to the stream. Comment the line below if you don't want to add new rules.
        await setRules();

    } catch (e) {
        console.error(e);
        process.exit(1);
    }

    // Listen to the stream.
    streamConnect(0);
})();