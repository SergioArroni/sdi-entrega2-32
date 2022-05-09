module.exports = {
    mongoClient: null, app: null, init: function (app, mongoClient,) {
        this.mongoClient = mongoClient;
        this.app = app;
    }, comprobarInvitacion: async function (filter) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'invitaciones';
            const invitacionesCollection = database.collection(collectionName);
            const invitaciones=invitacionesCollection.find(filter);
            const result = await invitaciones.toArray();
            return result.length>0;
        } catch (error) {
            throw (error);
        }
    }, insertarInvitacion: async function (id_from,id_to) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'invitaciones';
            const invitacionesCollection = database.collection(collectionName);
            var invitacion={id_from: id_from, id_to:id_to};
            const result=await invitacionesCollection.insertOne(invitacion);

            return result.insertedId;
        } catch (error) {
            throw (error);
        }
    }, getAllInvitacionesPg : async function (filter,pg, funcion) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'invitaciones';
            const invitacionesCollection = database.collection(collectionName);
            const invitacionesCount = invitacionesCollection.count();
            const invitaciones = invitacionesCollection.find(filter).skip((pg - 1) * 5).limit(5);
            const result = await invitaciones.toArray();

            return funcion(invitaciones, invitacionesCount);
        } catch (error) {
            throw (error);
        }
    }
};