module.exports = {
    mongoClient: null, app: null, init: function (app, mongoClient,) {
        this.mongoClient = mongoClient;
        this.app = app;
    }, insertPublicaction: async function (publication) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'publications';
            const publicationCollection = database.collection(collectionName);
            const result = await publicationCollection.insertOne(publication);
            return result.insertedId;
        } catch (error) {
            throw (error);
        }
    }, getPublications: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'publications';
            const publicationCollection = database.collection(collectionName);
            const publications = await publicationCollection.find(filter, options).toArray();
            return publications;
        } catch (error) {
            throw (error);
        }
    }, getPublicationsPg: async function (filter, options, page) {
        try {
            const limit = 2;
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'publications';
            const publicationsCollection = database.collection(collectionName);
            const publicationsCollectionCount = await publicationsCollection.find(filter, options).toArray();
            const cursor = publicationsCollection.find(filter, options).skip((page - 1) * limit).limit(limit)
            const publications = await cursor.toArray();
            const result = {publications: publications, total: publicationsCollectionCount.length};
            return result;
        } catch (error) {
            throw (error);
        }
    }, getAllPublicacionesPg : async function (filter,page, funcion) {
    try {
        const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
        const database = client.db("Cluster0");
        const collectionName = 'publications';
        const publicacionesCollection = database.collection(collectionName);
       ;
        const cursor = publicacionesCollection.find(filter);
        const publicaciones = await cursor.toArray();
        const allPublicaciones=publicaciones;
        const publicacionesPg=publicaciones.slice((page-1) * 5, (page * 5));

        funcion(publicacionesPg,allPublicaciones);
        //return funcion(result, invitacionesCount);
    } catch (error) {
        throw (error);
    }

}
};