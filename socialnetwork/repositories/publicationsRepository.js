module.exports = {
    mongoClient: null, app: null, init: function (app, mongoClient) {
        this.mongoClient = mongoClient;
        this.app = app;
    }, insertPublicaction: async function (publication) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'publications';
            const usersCollection = database.collection(collectionName);
            const result = await usersCollection.insertOne(publication);
            return result.insertedId;
        } catch (error) {
            throw (error);
        }
    }, getPublications: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'publications';
            const purchasesCollection = database.collection(collectionName);
            const purchases = await purchasesCollection.find(filter, options).toArray();
            return purchases;
        } catch (error) {
            throw (error);
        }
    }, getPublicationsPg: async function (filter, options, page) {
        try {
            const limit = 5;
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'publications';
            const publicationsCollection = database.collection(collectionName);
            const publicationsCollectionCount = await publicationsCollection.count();
            const cursor = publicationsCollection.find(filter, options).skip((page - 1) * limit).limit(limit)
            const publications = await cursor.toArray();
            const result = {publications: publications, total: publicationsCollectionCount};
            return result;
        } catch (error) {
            throw (error);
        }
    },
};