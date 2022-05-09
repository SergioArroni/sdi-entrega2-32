module.exports = {
    mongoClient: null, app: null, init: function (app, mongoClient) {
        this.mongoClient = mongoClient;
        this.app = app;
    }, getFriends: async function (filter1, filter2, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'friends';
            const friendsCollection = database.collection(collectionName);
            const friends = await friendsCollection.find(filter1, options).sort({"name": 1}).toArray();
            const friends2 = await friendsCollection.find(filter2, options).sort({"name": 1}).toArray();
            const totalFriends = friends.concat(friends2);
            return totalFriends;
        } catch (error) {
            throw (error);
        }
    },getAllFriends: async function (){
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'friends';
            const friendsCollection = database.collection(collectionName);
            const friends=friendsCollection.find({});
            const totalFriends = await friends.toArray();
            return totalFriends;
        } catch (error) {
            throw (error);
        }
    },comprobarAmistad: async  function (filter){
            try {
                const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
                const database = client.db("Cluster0");
                const collectionName = 'friends';
                const friendsCollection = database.collection(collectionName);
                const friends = friendsCollection.find(filter);
                const totalFriends=await  friends.toArray();
                return totalFriends.length>0;
            } catch (error) {
                throw (error);
            }
        }
    };