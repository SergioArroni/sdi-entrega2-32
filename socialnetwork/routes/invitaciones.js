module.exports=function (app,invitacionRepository,friendsRepository,usersRepository){
    app.get("/users/invitar/:id", function (req, res){

        let filter1={$or:[
                {$and: [{"id_from": req.session.user._id}, {"id_to": req.params.id}]},
                {$and: [{"id_to": req.session.user._id}, {"id_from": req.params.id}]}
            ]}
        console.log(req.session.user._id);
        friendsRepository.comprobarAmistad(filter1).then(boolAmigos=>{
            if(boolAmigos){
                res.redirect("/listInvitaciones" + "?mensaje=Ya es amigo de esa persona" + "&tipoMensaje=alert-danger ");
            }else{
                let filter2={$or:[
                        {$and: [{"id_from": req.session.user._id}, {"id_to": req.params.id}]},
                        {$and: [{"id_to": req.session.user._id}, {"id_from": req.params.id}]}
                    ]}
                invitacionRepository.comprobarInvitacion(filter2).then(hayInvitacion=>{
                    if(hayInvitacion) {
                        res.redirect("/users/listUsers" + "?mensaje=Hay invitaciones pendientes entre usted y esa persona" + "&tipoMensaje=alert-danger ");
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
}
