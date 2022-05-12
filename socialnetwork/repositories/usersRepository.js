const {all} = require("express/lib/application");
module.exports = {
    mongoClient: null, app: null, init: function (app, mongoClient) {
        this.mongoClient = mongoClient;
        this.app = app;

        /**
         *  @param funcion  devuelve una lista de usuarios donde podemos incluir filtros. La lista
         *                  será devuelta ordenada por nombre
         */
    }, getUsers: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'users';
            const usersCollection = database.collection(collectionName);
            const users = await usersCollection.find(filter, options).sort({"name": 1}).toArray();
            return users;
        } catch (error) {
            throw (error);
        }
    }, findDeleteUser: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'users';
            const usersCollection = database.collection(collectionName);
            const result = await usersCollection.findOneAndDelete(filter, options);
            return result;
        } catch (error) {
            throw (error);
        }
        /**
         *  @param funcion  Busca UN usuario y lo devuelve aplicando un filtro.
         */
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

        /**
         *  @param funcion  Dada una lista de ids, devolverá la lista de usuarios correspondiente.
         *                  La lista no incluirá contraseñas ni roles.
         */
    }, getFriends: async function(ids) {
        let users = [];
        let options = { sort: { "name": 1 }, projection: { email: 1,name : 1, surname : 1} };
        for(let i = 0; i < ids.length; i++) {
            try {
                let filter = {_id: ids[i]};
                const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
                const database = client.db("Cluster0");
                const collectionName = 'users';
                const usersCollection = database.collection(collectionName);
                const user = await usersCollection.find(filter, options).toArray();
                users.push(user[0]);
            } catch (error) {
                throw (error);
            }
        }
        return users;
        /**
         *  @param funcion  Inserta un usuario en la colección de usuarios.
         */
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
    /**
     *  @param funcion  Inserta un usuario en la colección de mensajes.
     */
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
    /**
     *  @param funcion  Devuelve una lista de mensajes. Normalmente se pasará un ID o dos para sacar
     *                  los mensajes entre dos usuarios
     */
    getMessage: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'messages';
            const messagesCollection = database.collection(collectionName);
            const message = await messagesCollection.findOne(filter, options);
            return message;
        } catch (error) {
            throw (error);
        }
    }, readMessage: async function (message, filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'messages';
            const messagesCollection = database.collection(collectionName);
            let result = await messagesCollection.updateOne(filter, {$set: message}, options);
            return result;
        } catch (error) {
            throw (error);
        }
    }, readMessages: async function (message, filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'messages';
            const messagesCollection = database.collection(collectionName);
            let result = await messagesCollection.updateMany(filter, {$set: message}, options);
            return result;
        } catch (error) {
            throw (error);
        }
    }, getMessages: async function (filter1, filter2, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'messages';
            const messagesCollection = database.collection(collectionName);

            const messages = await messagesCollection.find({
                $or: [filter1, filter2]
            }, {}).sort({date: 1}).toArray();

            return messages;
        } catch (error) {
            throw (error);
        }
    }, getAllUsersPg: async function (filter, page, user, funcion) {
        try {
            const limit = 5;
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("Cluster0");
            const collectionName = 'users';
            const usersCollection = database.collection(collectionName);
            const usersCollectionCount = await usersCollection.count();


            const cursor = usersCollection.find(filter);
            const users = await cursor.toArray();

            for (let i = 0; i < users.length; i++) {
                if (users[i].email === user.email) {
                    users.splice(i, 1);
                } else if (users[i].email === 'admin@email.com') {
                    users.splice(i, 1);
                }
            }
            const allusers = users;
            const usersFilter = users.slice((page - 1) * limit, (page * limit));

            const result = {users: usersFilter, total: usersCollectionCount};
            funcion(result, allusers);


        } catch (error) {
            throw (error);
        }
        /**
         *  @param funcion  Devuelve una lista de usuarios paginada (5 usuarios según la página)
         *                  dada una de Ids.
         */
    },getFriendsPg: async function(ids, page) {
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
        return {users: cursor, total: users.length};
    }
};
