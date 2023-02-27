const express = require('express');
const fs = require('fs')
const path = require('path');
const hbs = require('express-handlebars');
const {MongoClient} = require('mongodb');
const uri = "mongodb+srv://admin:admin1@clusty.gehnm.mongodb.net/database?retryWrites=true&w=majority";
const client = new MongoClient(uri);

const app = express();
const port = 8080;

app.use(express.json());
app.use(express.urlencoded({extended: true}));
app.use(express.static(path.join(__dirname, 'public')));

app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'hbs');
app.engine('hbs', hbs.engine({
  extname: 'hbs',
  defaultLayout: 'layout',
  layoutsDir: __dirname + '/views/layout/',
  partialsDir: __dirname + '/views/partials'
}))

  app.get('/', function (req, res) {
    res.render("home.hbs", {});
  });

  app.post("/getResults", function(req, res){
  //   const uri = "mongodb+srv://admin:admin1@clusty.gehnm.mongodb.net/database?retryWrites=true&w=majority";
  //   const client = new MongoClient(uri);
  //   const db = client.db('database');

  //   var list = await getList();
  //   var output = [];

  //   for(var i = 0; i < list.size(); ++i) {
  //       try {
  //           var currTweet = db.collection("Tweets").findOne({ 'data.id': list[i]})
  //           output[i] = "AUTHOR: " + currTweet.data.author_id + "\n" + "TEXT: " + currTweet.data.text + "\n" + "HASHTAGS: " + currTweet.data.entities.hashtags + "\n\n\n";
  //       }
  //       catch (e) {
  //           console.log(e);
  //       }
  //       console.log(req.body.query);
  //   }
  //   res.send(output);
  })
  
  async function getList() {
    var link = 'lucene endpoint' + "?query=" + req.body.query
    let response = await fetch(link);
    let data = await response.json();
    return data
  }
  app.listen(port, () => console.log(`Server listening on http://localhost:${port}`));