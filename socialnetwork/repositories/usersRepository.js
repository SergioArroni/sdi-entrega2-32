module.exports = {
    mongoClient: null, app: null, init: function (app, mongoClient) {
        this.mongoClient = mongoClient;
        this.app = app;
    }, getUsers: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'users';
            const usersCollection = database.collection(collectionName);
            const users = await usersCollection.find(filter, options).toArray();
            //this.logger.debug("getUsers request");
            return users;
        } catch (error) {
            //this.logger.error("Error, getUsers");
            throw (error);
        }
    }, deleteUser: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'users';
            const usersCollection = database.collection(collectionName);
            const result = await usersCollection.deleteOne(filter, options);
            //this.logger.debug("deleteUser request");
            return result;
        } catch (error) {
            //this.logger.error("Error, deleteUser");
            throw (error);
        }
    }, findDeleteUser: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'users';
            const usersCollection = database.collection(collectionName);
            const result = await songsCollection.findOneAndDelete(filter, options);
            //this.logger.debug("findDeleteUser request");
            return result;
        } catch (error) {
            //this.logger.error("Error, findDeleteUser");
            throw (error);
        }
    }, findUser: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'users';
            const usersCollection = database.collection(collectionName);
            const user = await usersCollection.findOne(filter, options);
            //this.logger.debug("findUser request");
            return user;
        } catch (error) {
            //this.logger.error("Error, findUser");
            throw (error);
        }
    }, getFriends: async function(ids) {
        let users = new Array();
        for(let i = 0; i < ids.length; i++) {
            try {
                let filter = {_id: ids[i]};
                let options = {};
                const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
                const database = client.db("Cluster0");
                const collectionName = 'users';
                const usersCollection = database.collection(collectionName);
                const user = await usersCollection.findOne(filter, options);
                //this.logger.debug("getFriends request");
                users.push(user);
            } catch (error) {
                //this.logger.error("Error, getFriends");
                throw (error);
            }
        }
        return users;
    }, insertUser: async function (user) {
            try {
                const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
                const database = client.db("Cluster0");
                const collectionName = 'users';
                const usersCollection = database.collection(collectionName);
                const result = await usersCollection.insertOne(user);
                //this.logger.debug("getFriends request");
                return result.insertedId;
            } catch (error) {
                //this.logger.error("Error, insertUser");
                throw (error);
            }
        },
    insertMessage: async function (message, callbackFunction) {
        this.mongoClient.connect(this.app.get('connectionStrings'), function (err, dbClient) {
            if (err) {
                //this.logger.error("Error, insertMessage");
                callbackFunction(null)
            } else {
                const database = dbClient.db("Cluster0");
                const collectionName = 'messages';
                const messageCollection = database.collection(collectionName);
                messageCollection.insertOne(message)
                    .then(result => callbackFunction(result.insertedId))
                    .then(() => dbClient.close())
                    .catch(err => callbackFunction({error: err.message}));
                //this.logger.debug("getFriends request");
            }
        });
    },
    getMessages: async function (filter1, filter2, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'messages';
            const messagesCollection = database.collection(collectionName);
            const message = await messagesCollection.find(filter1, options).toArray();
            const message2 = await messagesCollection.find(filter2, options).toArray();
            const totalMessages = message.concat(message2);
            //this.logger.debug("getMessages request");
            return totalMessages;
        } catch (error) {
            //this.logger.error("Error, getMessages");
            throw (error);
        }
    }, getUsersPg: async function (filter, options, page) {
        try {
            const limit = 4;
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'users';
            const usersCollection = database.collection(collectionName);
            const usersCollectionCount = await usersCollection.count();
            const cursor = usersCollection.find(filter, options).skip((page - 1) * limit).limit(limit)
            const users = await cursor.toArray();
            const result = {users: users, total: usersCollectionCount};
            //this.logger.debug("getUsersPg request");
            return result;
        } catch (error) {
            //this.logger.error("Error, getUsersPg");
            throw (error);
        }
    }, getFriendsPg: async function(ids, page) {
        let users = new Array();
        const limit = 5;
        for (let i = 0; i < ids.length; i++) {
            try {
                let filter = {_id: ids[i]};
                let options = {};
                const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
                const database = client.db("Cluster0");
                const collectionName = 'users';
                const usersCollection = database.collection(collectionName);
                const user = await usersCollection.findOne(filter, options);
                users.push(user);
                //this.logger.debug("getFriendsPg request");
            } catch (error) {
                //this.logger.error("Error, getFriendsPg");
                throw (error);
            }
        }
        const cursor = users.slice((page - 1) * limit, limit + 1);
        const result = {users: cursor, total: users.length};
        return result;
    }
};