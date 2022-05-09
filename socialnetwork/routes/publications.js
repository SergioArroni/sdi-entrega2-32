const {ObjectID} = require("mongodb");
const {all} = require("express/lib/application");
module.exports=function (app,publicationsRepository) {
    app.get("/publications/listPublicaciones", function (req, res) {
        let filter = {user: new ObjectID(req.session.user._id)};
        let page = parseInt(req.query.page);
        if (typeof req.query.page === "undefined" || req.query.page === null || req.query.page === "0") {
            page = 1;
        }
        publicationsRepository.getAllPublicacionesPg(filter, page, function (p, allpublicaciones) {
            console.log(p);
            let lastPage = (allpublicaciones.length) / 5;
            if ((allpublicaciones.length) % 5 > 0) {
                lastPage = lastPage + 1;
            }
            let pages = [];
            for (let i = page - 2; i <= page + 2; i++) {
                if (i > 0 && i <= lastPage) {
                    pages.push(i);
                }
            }
            let response = {
                publicaciones: p,
                user:req.session.user,
                pages: pages,
                currentPage: page,
                session: req.session.user
            }
            res.render("publications/listPublicaciones.twig", response);

        });

    });
}