const express = require('express');
const app = express();
app.use(express.static('public'));

const http = require('http');
const {Server} = require("socket.io");

const cors = require('cors');
app.use(cors());

const server = http.createServer(app);

const io = new Server(server, {
    cors: {
        origin: '*',
        methods: ["GET", "POST"]
    }
});

const bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended : true }));
app.use(express.json());

// const customEnv = require('custom-env');
// customEnv.env(process.env.NODE_ENV, './config');
// console.log("mongodb://127.0.0.1:27017/WebChat");
// console.log(5000);

const mongoose = require('mongoose');
mongoose.connect("mongodb://127.0.0.1:27017/WebChat", {
    useNewUrlParser: true,
    useUnifiedTopology: true
}).then(() => {
    console.log("Connected to MongoDB");
}).catch((error) => {
    console.log("Error connecting to MongoDB:", error);
});

app.use(express.static('public'));

const user = require('./routes/user');
const token = require('./routes/token');
const chat = require('./routes/chat');
app.use('/api/Users', user);
app.use('/api/Tokens', token);
app.use('/api/Chats', chat);

const admin = require('firebase-admin');
const serviceAccount = require('./firebaseKey.json');
const socketsManager = require('./socketManager');
const androidTokens = require('./androidTokens');

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
});

// socket communication
const connectedUsers = new Map();

io.on('connection', (socket) => {

    socket.on("connecting", (username) => {
        socket.join(username);
        socketsManager[username] = socket;
        delete androidTokens[username];
    })

   socket.on("login", (username) => {
    connectedUsers.set(username, socket);
    
   })

   socket.on("send", (data) => {
    for (const contact of connectedUsers.keys()) {
        if (contact === data.receiver) {
            const recSocket = connectedUsers.get(data.receiver);
            if (recSocket) {
                socket.to(recSocket.id).emit("receive", data.sender);
            }
        }
    }
   })

   socket.on("logout", (username) => {
    connectedUsers.delete(username);
   })
})
server.listen(5000);