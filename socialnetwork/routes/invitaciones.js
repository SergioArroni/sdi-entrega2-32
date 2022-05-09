module.exports=function (app,invitacionRepository,friendsRepository,usersRepository){
    app.get("/users/invitar/:id", function (req, res){

        let filter1={$or:[
                {$and: [{"id_from": req.session.user._id}, {"id_to": req.params.id}]},
                {$and: [{"id_to": req.session.user._id}, {"id_from": req.params.id}]}
            ]};
        friendsRepository.comprobarAmistad(filter1).then(boolAmigos=>{
            if(boolAmigos){
                res.send("Ya son amigos");
            }else{
                let filter2={$or:[
                        {$and: [{"id_from": req.session.user._id}, {"id_to": req.params.id}]},
                        {$and: [{"id_to": req.session.user._id}, {"id_from": req.params.id}]}
                    ]}
                invitacionRepository.comprobarInvitacion(filter2).then(hayInvitacion=>{
                    if(hayInvitacion) {
                        res.send("Ya hay invitacion");
                    }else{
                        invitacionRepository.insertarInvitacion(req.session.user._id,req.params.id).then(idInvitacion=>{
                            if(idInvitacion!==null)
                                res.redirect("/users/listUsers?mensaje=Invitación enviada correctamente&tipoMensaje=alert-success");
                        });

                    }
                });
            }
        });


    });

    app.get("/invitaciones/listInvitaciones",function (req, res){
       let filter={id_to:req.session.user._id};
       let page = parseInt(req.query.page);
       if (typeof req.query.page === "undefined" || req.query.page === null || req.query.page === "0") {
           page = 1;
       }
       invitacionRepository.getAllInvitacionesPg(filter,page,function (invitaciones, allInvitaciones){

           let lastPage=(allInvitaciones.length)/5;
           if((allInvitaciones.length)%5>0){
               lastPage=lastPage+1;
           }
           let pages=[];
           for(let i=page-2;i<=page+2;i++){
               if(i>0 && i<=lastPage){
                   pages.push(i);
               }
           }
           usersRepository.getUsers({},{}).then(users=>{
              let response={
                  users:users,
                  invitaciones:invitaciones,
                  pages:pages,
                  currentPage:page,
                  session:req.session.user
              }
               res.render("invitaciones/listInvitaciones.twig", response);
           });
       });
    });

    app.get("/invitaciones/aceptar/:id",function (req,res){
       let filter={"_id": req.params.id};
       usersRepository.getUsers(filter,{}).then(users=>{
          let filter2=  {$and: [
              {id_to:req.session.user._id},
              {id_from:req.params.id}
          ]};
          invitacionRepository.getInvitaciones(filter2).then(invitaciones=> {
              if (invitaciones.length == 0){
                res.send("No hay invitaciones");
              }
              let filter1={$or:[
                  {$and: [{"id_from": req.session.user._id}, {"id_to": req.params.id}]},
                  {$and: [{"id_to": req.session.user._id}, {"id_from": req.params.id}]}
              ]};
             friendsRepository.comprobarAmistad(filter1).then(boolAmigos=>{
                 if (!boolAmigos){
                     friendsRepository.insertarAmigos(req.session.user._id,req.params.id).then(idAmigo=>{
                         if(idAmigo==null)
                             res.send("Error al aceptar la peticion de amistad");
                        let filter4={$or: [
                            {$and: [
                                {"id_from": req.session.user._id},
                                {"id_to":req.params.id}
                            ]},
                            {$and: [
                                {"id_to": req.session.user._id},
                                {"id_from":req.params.id}
                            ]}
                        ]};
                        invitacionRepository.eliminarInvitacion(filter4).then(invitaciones=>{
                            if (invitaciones == null) {
                                res.send("No hay invitacion para eliminar");
                            } else {
                                res.redirect("/invitaciones/listInvitaciones?mensaje=Invitación aceptada correctamente&tipoMensaje=alert-success");
                            }
                         });
                     });
                 }
             });
          });
       });
    });

}
