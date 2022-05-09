module.exports=function (app,invitacionRepository,friendsRepository,usersRepository){
    app.get("/users/invitar/:id", function (req, res){

        let filter1={$or:[
                {$and: [{"id_from": req.session.user._id}, {"id_to": req.params.id}]},
                {$and: [{"id_to": req.session.user._id}, {"id_from": req.params.id}]}
            ]}
        console.log(req.session.user._id);
        friendsRepository.comprobarAmistad(filter1).then(boolAmigos=>{
            if(boolAmigos){
                res.redirect("/invitaciones" + "?mensaje=Ya es amigo de esa persona" + "&tipoMensaje=alert-danger ");
            }else{
                let filter2={$or:[
                        {$and: [{"id_from": req.session.user._id}, {"id_to": req.params.id}]},
                        {$and: [{"id_to": req.session.user._id}, {"id_from": req.params.id}]}
                    ]}
                invitacionRepository.comprobarInvitacion(filter2).then(hayInvitacion=>{
                    if(hayInvitacion) {
                        res.redirect("/listInvitaciones" + "?mensaje=Hay invitaciones pendientes entre usted y esa persona" + "&tipoMensaje=alert-danger ");
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
}
