module.exports = {
    mongoClient: null, app: null, init: function (app, mongoClient) {
        this.mongoClient = mongoClient;
        this.app = app;
    }, getFriends: async function (filter1, filter2, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'friends';
            const purchasesCollection = database.collection(collectionName);
            const purchases = await purchasesCollection.find(filter1, options).sort({"name": 1}).toArray();
            const purchases2 = await purchasesCollection.find(filter2, options).sort({"name": 1}).toArray();
            const totalPurchases = purchases.concat(purchases2);
            return totalPurchases;
        } catch (error) {
            throw (error);
        }
    }

    };