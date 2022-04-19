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
    }
};