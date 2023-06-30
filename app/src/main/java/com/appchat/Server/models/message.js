const mongoose = require('mongoose');
const User = require('./user');

const Schema = mongoose.Schema;

// defenitions of the structure of a message.
const Message = new Schema({
    id: {
        type: Number,
        required: true
    },
    created: {
        type: Date,
        default: Date.now
    },

    sender: [{ type: mongoose.Schema.Types.ObjectId, ref: 'User' }],
    
    content: {
        type: String,
        required: true
    }
});

// export the schema
module.exports = mongoose.model('Message', Message);