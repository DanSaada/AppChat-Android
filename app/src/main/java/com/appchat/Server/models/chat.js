const mongoose = require('mongoose');
const User = require('./user');
const Message = require('./message');
const Schema = mongoose.Schema;

// defenitions of the structure of a chat.
const Chat = new Schema({
    id: {
        type: Number,
        required: true
    },

    users: [{ type: mongoose.Schema.Types.ObjectId, ref: 'User' }],

    messages: [{ type: mongoose.Schema.Types.ObjectId, ref: 'Message' }]
});

// export the schema
module.exports = mongoose.model('Chat', Chat);

