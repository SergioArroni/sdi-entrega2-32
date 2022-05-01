module.exports = {
    mongoClient: null, app: null, init: function (app, mongoClient) {
        this.mongoClient = mongoClient;
        this.app = app;
    }, getUsers: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'users';
            const purchasesCollection = database.collection(collectionName);
            const purchases = await purchasesCollection.find(filter, options).toArray();
            return purchases;
        } catch (error) {
            throw (error);
        }
    }, deleteUser: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'users';
            const songsCollection = database.collection(collectionName);
            const result = await songsCollection.deleteOne(filter, options);
            return result;
        } catch (error) {
            throw (error);
        }
    }, findDeleteUser: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'users';
            const songsCollection = database.collection(collectionName);
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
                const songsCollection = database.collection(collectionName);
                songsCollection.insertOne(message)
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
            const purchasesCollection = database.collection(collectionName);
            const purchases = await purchasesCollection.find(filter1, options).toArray();
            const purchases2 = await purchasesCollection.find(filter2, options).toArray();
            const totalPurchases = purchases.concat(purchases2);
            return totalPurchases;
        } catch (error) {
            throw (error);
        }
    }
};