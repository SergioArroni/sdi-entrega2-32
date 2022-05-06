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

            return users;
        } catch (error) {

            throw (error);
        }
    }, deleteUser: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'users';
            const usersCollection = database.collection(collectionName);
            const result = await usersCollection.deleteOne(filter, options);

            return result;
        } catch (error) {

            throw (error);
        }
    }, findDeleteUser: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'users';
            const usersCollection = database.collection(collectionName);
            const result = await songsCollection.findOneAndDelete(filter, options);

            return result;
        } catch (error) {

            throw (error);
        }
    }, findUser: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'users';
            const usersCollection = database.collection(collectionName);
            const user = await usersCollection.findOne(filter, options);

            return user;
        } catch (error) {

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

                users.push(user);
            } catch (error) {

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

                return result.insertedId;
            } catch (error) {

                throw (error);
            }
        },
    insertMessage: async function (message, callbackFunction) {
        this.mongoClient.connect(this.app.get('connectionStrings'), function (err, dbClient) {
            if (err) {

                callbackFunction(null)
            } else {
                const database = dbClient.db("Cluster0");
                const collectionName = 'messages';
                const messageCollection = database.collection(collectionName);
                messageCollection.insertOne(message)
                    .then(result => callbackFunction(result.insertedId))
                    .then(() => dbClient.close())
                    .catch(err => callbackFunction({error: err.message}));

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

            return totalMessages;
        } catch (error) {

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

            return result;
        } catch (error) {

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

            } catch (error) {

                throw (error);
            }
        }
        const cursor = users.slice((page - 1) * limit, limit + 1);
        const result = {users: cursor, total: users.length};
        return result;
    }
};