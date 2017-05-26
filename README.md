# Gutenberg Semester Project
## Morten Schultz Laursen, Søren Tromborg Larsen og Uffe Nedergaard
## Test reflections:

[PDF - Reflections](https://github.com/Schultzz/GutenbergSemProject/blob/master/resources/Reflektion%20over%20l%C3%A6ringsm%C3%A5l%20i%20Test.pdf)

## Experiments

Query | Neo4j | MySql | Mongo
-|-|-|-
getCitiesByBookTitle | [230.20] ms | [243.80] ms | [133.20] ms
getBookTitlesByCityName | [4656.20] ms | [196.20] ms | [3026.80] ms
getCitiesByGeolocation | [295.00] ms  | [5066.80] ms | [4871.60] ms
getBooksByAuthorAndPlotCities |[82.80] ms | [140.20] ms | [67.20] ms
Total Average | 1316.05ms | 1397.4ms | 2024.7ms


# Data Modeling

## Mongo

Vores valg af datamodel, stod mellem to forskellige muligheder.
Første model består i to collections, en med bøger, en med byer.

![picture alt](https://github.com/Schultzz/GutenbergSemProject/blob/master/resources/mongo1.PNG?raw=true)

I book-dokumentet, holder vi information omkring bogens Id på Gutenberg, titel på bogen, en eller flere forfattere og listen af samtlige bynavne nævnt i bogen. Disse bynavne fungerer som reference til city-dokumentet, der indeholder yderligere information omkring byen(Også mere end vist i figuren). Dette vil betyde at vi ingen redundant data har i forhold til city-dokumenter. Altså en by optræde i mange bøger, men ved hjælp af referencer holder vi kun udvidet information om byen et sted.
 
Den anden model har kun én enkelt collection, og indeholder books med embedded-documents, i form af cities.

![picture alt](https://github.com/Schultzz/GutenbergSemProject/blob/master/resources/mongo2.PNG?raw=true)

Her indeholder bogen stadig samme information, forskellen er dog at denne datamodel indeholder et embedded document. Dette betyder at den information der før skulle hentes i en anden collection, er indlejret i selve book-dokumentet. Nu indeholder vores collection altså potentielt en masse redundant data, da en by der er nævnt i to forskellige bøger, er listet uafhængigt af hinanden.

Begge datamodellering er afprøvet og for dette projekt er der en klar vinder. Dog er der nogle fordele og ulemper ved at bruge referencer vs embedded documents.

### Fordele ved referencer er blandt andet at:
* Der undgås redundant data              
* Nemmere at opdatere dokumenter der ikke er indlejret i andre dokumenter. (Det er svært at sikre at at alt data er ens, samt at der bliver rettet alle de relevante steder)
* Simple import-scripts  

### Ulemper ved referencer:
* Det påvirker performance negativt at skulle lave forespørgsler til to collections (Trækker både mere på server, samt sænker user experience da der skal ventes længere tid)
* Skal der ændres på tværs af collections, skal der laves ekstra arbejde for at sikre atomicity.

### Fordele ved embedded documents:
* Potentiel hurtigere forespørgsler. Skal der søges på ting i det yderste document, for at finde data i det embedded, kan der med fordel bruges indexes. Dette vil være markant hurtigere end at lave to søgninger på tværs af collections.
* Håndterer one-to-many relations bedre, da denne relation passer godt til embedded documents, da der kan være tale om “parent/child”. Altså documentet er en parent, der kan have mange embedded-documents, altså childs.
* Atomicity er nemmere at implementere, da ændringer kun påvirker document/embedded document.

### Ulemper ved embedded documents:
* Skal der ofte ændres i dataen, kan det være bøvlet at sørge for at alle redundante entiteter bliver ændret ens.
* Er det tale om embedded documents med mange tilføjelser af information, skal man være opmærksom på størrelse af dokumenterne.
* Many-to-Many relations og komplicerede data set kan være vanskelige at oprette.

### Vores valg af datamodel

Vi startede med data model nummeret(referencer), da dette var det mest logiske da der skulle importeres data fra vores Book-scanner. Vores mindset er fra tidligere projekter nok også biased mod relationelle databaser, og det føltes derfor naturligt at lave en datamodel med en reference. Dog fandt vi hurtigt ud af at forespørgslerne blev meget langsomme, da en bog indeholder mange referencer til en by.
Ved at skifte datamodellen ud med embedded-documents, kan vi lave forespørgsler på indexes, fx titel og bookId. Herfra får vi så den information vi vil have om byerne (navn og geolocation) i samme forespørgsel. Dette gjorde en meget mærkbar forskel. Dog er kørselstiden stadig langsom når der skal søges på information om byerne. Da de ligger i store lister, kan de ikke indexeres ordentligt og mongo skal derfor søge alle dokumenter’s lister igennem, hvilket tager tid på et stort dataset.

- - - -
## MySQL

Da domænet for opgaver er relativt lille, har vi tre entiteter Author, Book og Location. De har
henholdsvis en har mange til mange relationer mellem Author og Book samt Book og Location.
Se nedenstående relationel skema diagram. 

![picture alt](https://github.com/Schultzz/GutenbergSemProject/blob/master/resources/mysql.PNG?raw=true)

Der er i alt fem tabeller, som er modelleret efter et relationelt design. Kardinalitet mellem tabellen er en mange til mange relation, derfor har vi oprettet to nye join tabeller hvor relationen bliver holdt.

Vi har oprette contrains som primary- og foreing keys, som fungere som en unik nøgle til at identificere de enkelte rækker i tabellerne. Alle tabeller på nær Location, benytter et id som nøgle, hvor vi i stedet valgte at benytte bynavnet som primær nøgle. Det gjorde vi på trods af flere byer kan have samme navn, men ud fra de bynavne vi udtrak fra bøgerne, kunne vi ikke sige hvilken ”Lyngby” der var tale om. Formålet var så at vi kunne lave et index på bynavnet, som gjorde queries på bynavnet mere effektive og hurtige.

For at kunne løse den query, hvor alle bøger indenfor en given radius fra et koordinat, brugte vi mysql geometri type. Her kan man indsætte et punkt/koordinat, hvorpå man kan udregne distancer, radius mm. I version 5.6 og frem, kommer mysql med ”Spatial Relation Functions” som er funktioner der kan udregne forskellige ting ud fra koordinater. Vi brugte st_distance_sphere til at få bøger i en given radius. Metoder kræver et geometri punkt, bestående af et punkt ”point(12.568337, 55.676098)”. Se query nedefor.

![picture alt](https://github.com/Schultzz/GutenbergSemProject/blob/master/resources/mysqlQuery.PNG?raw=true)

- - - -
## Neo4j

I Neo4j har vi valgt at lave tre nodes og to relationships:
 
:Book(id, title)
:Author(id, name)
:City(name, latitude, longitude)
(:Book)-[:MENTIONS]->(:City)
(:Book)-[:AUTHORED_BY]->(:Author)
 
En fuld graf ser sådan ud:

![picture alt](https://github.com/Schultzz/GutenbergSemProject/blob/master/resources/neo4jdm.PNG?raw=true)

En anden mulighed havde været at have informationer om forfatteren på book-noden også, da det kun er forfatterens navn der skal stå med. Så havde vi ikke behøvet at lave mere end én relation, den imellem City og Book. Det ville dog have besværliggjort processen med at indsætte data fra csv-filerne, da nogle af bøgerne har flere forfattere. 
 
Det havde også givet redundant data, da en forfatter der havde skrevet flere bøger ville have hans navn på samtlige af de bøger. På denne måde kan vi nemt finde ud af hvilke bøger en bestemt forfatter har skrevet, og samtidig opretholde første normalform ved at fjerne redundant data og oprette en særskilt node for hver type af data.

- - - -
## Importering af data

Da vi har erfaret ved tidligere database projekter, hvordan store mængder data kan tage forholdsvis langtid at importeret, besluttede vi at benytte database import-værktøjerne. De forskellige database import-værktøjer understøtte som oftest standart formater som csv, tsv, json mm. Vi lavede derfor nogle generiske csv filer, vi alle kunne importere via import-værktøjet. Det havde flere fordele blandt andet var vores skanner var ret effektiv, da den kun skulle skrive til filer og ikke håndtere forbindelser til databaser mm.

Vi valgte at køre alle databaser på en samlet virtual maskine, hvor vores csv filerne blev ekspoteret til. Vi havde derefter lavet et shell script der kunne sætte filerne ind. Scriptet stod for at hente csvfilerne ned, sætte headers på dem, og køre et script til hver database i de respektive sprog. At køre alle scripts tog lidt under 10 minutter. Neo4j tog længst tid, med 5 minutter. MySql med 3 minutter og Mongo tog kun omkring 10 sekunder. Denne løsning sikre os at vi kan effektivt importere op imod 10.000.000 rækker data på relativt kort tid, sammenlignet med programmatisk import, f.eks. JDBC.

- - - -
## Vores Anbefaling

Vores korte svar vil være at benytte en relationel database. Dette er dog et meget unuanceret svar. Hver databasetype har sine egne fordele og ulemper. Kigger vi vores eksperiment, ses det tydelige hvor de forskellige typer er stærke. Hvis hastighed var det eneste parameter der skulle måles på, ville en applikation med alle tre databasetyper være den klart bedste løsning. Vi kan tydeligt se Mongo er enormt hurtig i forespørgsler på værdier der kan indekseres. Mongo er til gengæld langsom til at søge i store lister af embedded-documents. Når der skal søges på byer skal alt søges igennem. Samme problem har Neo, da der er kompliceret at søge alle noders relationer igennem hurtigt. MySQL har en styrke i relationer, og klarer derfor denne query markant hurtigere end de to andre typer. Neo har et ekstremt hurtigt API til at søge på geolocations, og her vinder Neo stort over de to andre. Mongo har også et API til at søge i geolocations, men i dette tilfælde ligger informationen i embedded-documents, og som nævnt før performer mongo dårligt her.
Neo’s store styrke er at kunne håndtere dybe søgninger, altså mange joins. Dette er ikke tilfælde i dette projekt. Havde der været dybere søgninger såsom, “Find alle byer indenfor en radius, der optræder i bøger hvor en bestemt anden by også optræder, skrevet af en bestemt forfatter”. Her vil Neo kunne begynde at vinde over SQL og Mongo på store dataset.

Så hvis vi ikke laver en applikation der benytter alle typer, og lader dem håndterer forskellige forespørgsler, ville vi altså vælge MySql. Det har en stærk struktur, gode muligheder for at indsætte og opdatere data og overall en fornuftigt average søgetid.
