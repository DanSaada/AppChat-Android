const mongoose = require('mongoose');

const Schema = mongoose.Schema;

// defenitions of the structure of a user.
const User = new Schema({
    username: {
        type: String,
        required: true
    },
    password: {
        type: String,
        required: true
    },
    displayName: {
        type: String,
        required: true
    },
    profilePic: {
        type: String,
        required: true
    }
});

// export the schema
module.exports = mongoose.model('User', User);

