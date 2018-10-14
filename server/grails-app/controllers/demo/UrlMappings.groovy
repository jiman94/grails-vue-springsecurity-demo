package demo

class UrlMappings {

    static mappings = {
        delete "/$controller/$id(.$format)?"(action:"delete")
        get "/$controller(.$format)?"(action:"index")
        get "/$controller/$id(.$format)?"(action:"show")
        post "/$controller(.$format)?"(action:"save")
        put "/$controller/$id(.$format)?"(action:"update")
        patch "/$controller/$id(.$format)?"(action:"patch")
        "/api/vehicle"(resources:'vehicle')
        "/api/driver"(resources:'driver')
        "/api/searchUser"(controller:'driver', action: 'username')
        "/api/exportVehicle"(controller:'vehicle', action: 'export')
        "/api/exportXls"(controller:'vehicle', action: 'exportXls')
        get "/api/customRest"(controller:'customRest', action:'index')
        post "/api/customRest"(controller:'customRest',action:'save')
        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
