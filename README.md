# microservice
A sample project to learn about REST APIs (JAX-RS), database persistence (JPA), validation, liquibase, etc.

You can run it in any JakartaEE server, such as Payara or OpenLiberty and then test it by going to http://localhost:8080/resources/buildings

To update you can use something like (the version must be the current one to avoid OptimisticLockException, each update increments the version):

    ```javascript
    fetch('http://localhost:8080/microservice/resources/buildings/00000000-0000-0000-0000-000000000000', {
        method: "PUT",
        mode: "cors", // no-cors, *cors, same-origin
        cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
        credentials: "same-origin", // include, *same-origin, omit
        headers: {
          "Content-Type": "application/json",
        },
        redirect: "follow",
        referrerPolicy: "no-referrer", // no-referrer, *no-referrer-when-downgrade, origin, origin-when-cross-origin, same-origin, strict-origin, strict-origin-when-cross-origin, unsafe-url
        body: '{"id":"00000000-0000-0000-0000-000000000000","version":0,"name":"Rainbow Tower","address":"Hobbiton, The Shire","floors":[{"id":"ffffffff-ffff-ffff-ffff-ffffffffffff","version":0,"number":"14","desks":[{"id":"00000000-0000-0000-0000-000000000009","version":0,"number":"14.001B","available":true},{"id":"00000000-0000-0000-0000-00000000000a","version":0,"number":"14.002A","available":true},{"id":"00000000-0000-0000-0000-00000000000b","version":0,"number":"14.002B","available":true},{"id":"00000000-0000-0000-0000-000000000008","version":0,"number":"14.001A","available":true}]},{"id":"00000000-0000-0000-ffff-ffffffffffff","version":0,"number":"11","desks":[{"id":"00000000-0000-0000-0000-000000000004","version":0,"number":"11.001A","available":true},{"id":"00000000-0000-0000-0000-000000000005","version":0,"number":"11.001B","available":true},{"id":"00000000-0000-0000-0000-000000000006","version":0,"number":"11.002A","available":true},{"id":"00000000-0000-0000-0000-000000000007","version":0,"number":"11.002B","available":true}]}]}'
    });
    ```