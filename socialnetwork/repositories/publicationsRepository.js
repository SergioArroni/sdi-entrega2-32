module.exports = {
    mongoClient: null, app: null, init: function (app, mongoClient,) {
        this.mongoClient = mongoClient;
        this.app = app;
        /**
         *  @param funcion  Inserta una publicación en la lista de publicaciones.
         */
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
        /**
         *  @param funcion  Devuelve una lista de publicaciones.
         */
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
        /**
         *  @param funcion  Devuelve una lista de publicaciones paginada (5 publicaciones según la página)
         */
    }, getPublicationsPg: async function (filter, options, page) {
        try {
            const limit = 5;
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'publications';
            const publicationsCollection = database.collection(collectionName);
            const publicationsCollectionCount = await publicationsCollection.find(filter, options).toArray();
            const cursor = publicationsCollection.find(filter, options).skip((page - 1) * limit).limit(limit)
            const publications = await cursor.toArray();
            return {publications: publications, total: publicationsCollectionCount.length};
        } catch (error) {
            throw (error);
        }
    },/**
     *  @param funcion  Busca publicaciones para la paginacion
     */
    getAllPublicacionesPg : async function (filter,page, funcion) {
    try {
        const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
        const database = client.db("Cluster0");
        const collectionName = 'publications';
        const publicacionesCollection = database.collection(collectionName);
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