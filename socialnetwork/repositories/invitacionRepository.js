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
    }, getAllInvitacionesPg : async function (filter,page, funcion) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'invitaciones';
            const invitacionesCollection = database.collection(collectionName);
            //const invitacionesCount = invitacionesCollection.count();
            const cursor = invitacionesCollection.find(filter);
            const invitaciones = await cursor.toArray();
            const allInvitaciones=invitaciones;
            const invitacionesPg=invitaciones.slice((page-1) * 5, (page * 5));

            funcion(invitacionesPg,allInvitaciones);
            //return funcion(result, invitacionesCount);
        } catch (error) {
            throw (error);
        }

    }, getInvitaciones: async  function (filter){
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'invitaciones';
            const invitacionesCollection = database.collection(collectionName);

            const invitaciones=invitacionesCollection.find(filter);
            const result=await invitaciones.toArray();
            return result;
        } catch (error) {
            throw (error);
        }
    }, eliminarInvitacion: async  function (filter){
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'invitaciones';
            const invitacionesCollection = database.collection(collectionName);

            const result=invitacionesCollection.remove(filter);
            return result;
        } catch (error) {
            throw (error);
        }
    }
};