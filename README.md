# CI455 Y2 SEM1

## Dependencies

- Any modern Java LTS build
- Node.js (npm, npx)
- Maven

## Usage

### Bash script

Run `./start`, which will automatically check for compile-time dependencies on PATH, download runtime dependencies and start the SpringBoot webserver.

### Manual

It is recommended to use the bash script. Otherwise:

- Download all web dependencies:
  - `cd web/` from project root
  - `yarn build`, or `npx yarn build` if you don't have yarn installed
- Get Java environment ready:
  - `mvn install exec:exec` downloads all dependencies, then executes `com.shr4pnel.clients.Setup.main()` which initializes the database using `classpath:com/shr4pnel/config/init.sql`
- Run the project:
  - `mvn spring-boot:run` starts the webserver, which serves the bundled React.js files at http://localhost:3000.

## Structure

### Module CI:

#### [com.shr4pnel.clients](src/main/java/com/shr4pnel/clients)

Former location of Swing clients. Now, contains all necessary Java setup code in [Setup.java](src/main/java/com/shr4pnel/clients/Setup.java)

#### [com.shr4pnel.db](src/main/java/com/shr4pnel/db)

Most importantly, this package contains the StockReader and StockReadWriter, which are used frequently to bridge between the database and REST API.

#### [com.shr4pnel.schemas](src/main/java/com/shr4pnel/schemas)

Contains Java beans, which cast JSON strings into Java objects. Used for POST endpoints in the REST API to make JSON useful.

These are also Swagger/OpenAPI3.x annotated schemas for use in the API spec.

#### [com.shr4pnel.middleware](src/main/java/com/shr4pnel/middleware)

Heavily trimmed down compared to original codebase. RMI is vulnerability prone, causing the [worst CVE probably ever](https://nvd.nist.gov/vuln/detail/cve-2021-44228), and essentially grandfathered. 

[LocalMIddleFactory](src/main/java/com/shr4pnel/middleware/LocalMiddleFactory.java) remains to handle instantiation of StockR's and StockRW's, albiet heavily trimmed down. The server, and all stubs for netcode are all removed in favour of the REST API interacting with the DB in [WebApplication](src/main/java/com/shr4pnel/web/WebApplication.java) using StockReaders and StockReadWriters.

#### [com.shr4pnel.web](src/main/java/com/shr4pnel/web)

This subpackage is a single class, [WebApplication.java](src/main/java/com/shr4pnel/web/WebApplication.java), which handles hosting the web client for usage of the Ministore. It also hosts the REST API which the web client interracts with to check things like the stock levels. 

---

### [web/](web/)

Project files for the web frontend, which the end user interracts with. This is what replaces the previous usage of Swing/AWT.

#### [web/index.html](web/index.html)

The index of the web client. main.jsx is pulled in via a script tag here.

#### [web/src/main.jsx](web/src/main.jsx)

This is the file that creates the React component tree, and calls the parent component, App.jsx.

#### [web/src/App.jsx](web/src/App.jsx)

The parent component, which contains a panel with 4 seperate tabs which can be navigated to by the user. Each tab opens a pane, with different functions that can be accessed and switched between.

This component primarily handles managing  important state variables representing the amount of items in the system, as well as the items which need to be packed, seamlessly refetching data whenever the user switches panels using React's re-rendering.


### [web/src/components]

TBC.

## OpenAPI
- OpenAPI JSON documentation accessible at http://localhost:3000/docs
- Swagger UI for OpenAPI accessible at http://localhost:3000/swagger-ui/index.html

## Credits/Attribution

See [NOTICE](NOTICE)