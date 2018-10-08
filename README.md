# Performance testing 
##### Philip West Christiansen
##### Efterår 2018, Til: Kasper Østerbye, Afleveret: 07/10/2018

# Indholdsfortegnelse  
<!--ts-->
  * [Opgavebeskrivelse](#opgavebeskrivelse)  
  * [Introduktion og valg af opgave](#introduktion-og-valg-af-opgave)  
  * [Valg af databaser](#valg-af-databaser)  
      * [Relationel database](#relationel-database)  
      * [Graph database](#graph-database)  
  * [Datamodellering i databaser](#datamodellering-i-databaser)  
      * [PostgreSQL](#postgresql)
      * [Neo4j](#neo4j)
<!--ts-->



# Opgavebeskrivelse
You should try to use PMD on a larger example. If you have a project lying around from earlier projects, feel free to use that (best option). If not, this project on github, GraphQL for JPA has a suited level of complexity, and it is not quite clear if it well done, or just functional.

# Introduktion og valg af opgave
I denne opgave anvendes PMD på et "real life project". Projektet er et  system med et grafisk interface. Her kan billeder af biler hvor nummerpladen ses importeres. Systemet kan efter nærmere analyse af billedet returnere hvad der står på nummerpladen. 

# Opgave 1

- Run PMD on your chosen project.
- As there are 100s of different metrics (in the different subcategories), you have to argue for which 10 you pick. In particular what situation would make those you pick particular relevant.

## Svar

Først hvad er PMD, og hvorfor kan det anvendes effektivt? Officielt står PMD ikke for noget fast. Det har mange uofcielle navne. Mit ynglings er "Programming Mistake Detector". Dette navn giver en meget god indikation af hvad værktøjet kan anvendes til. Kort fortalt er PMD en static source code analyser, som kan anvendes til at "fejl" i kode. Det gør værktøjet ved brug af pre-defineret reglsæt som køres gennem ens kode, for så derefter at komme med en evaluering af koden. 

Jeg har valgt at benytte mig at NetBeans IDE 8.2 tl at afprøve PMD værktøjet. Her installeres et plugin EasyPMD som ses nedenfor:

![alt text](https://github.com/pwestdk/Real_life_code/blob/master/images/pmd_plugin.png)

Når jeg kører EasyPMD på mit projekt beskrevet ovenfor, får jeg følgende respons: 

![alt text](https://github.com/pwestdk/Real_life_code/blob/master/images/pmd1.png)
![alt text](https://github.com/pwestdk/Real_life_code/blob/master/images/pmd2.png)

EasyPMD returnere ikke "særlig mange" hævede pejefingere, men her vil jeg gå i dybden med 10 af dem:

1. Avoid using a branching statement as the last in loop.

Den her markere EasyPMD med rød, det vil sige at den skal man særligt være opmærksom på. Når jeg kigger på min koder kan jeg også godt se at loopet absolut ikke er nødvendigt. Det vil nemlig afslutte med det samme uanset hvad. Istedet ville det her nok give mest mening at bruge en if-statement, dette ville spare en masse tid, det er mere læsbar kode og det giver orverordnet også meget bedre mening.  

2. Avoid unused method parameters such as 'evt'.

Her siger EasyPMD at evt ikke er et godt nok navn til en property. Det er et "ActionEvent" der bliver kaldt for evt. Det ville her nok give mere mening at navngivet med "actionEvent". Dette fortæller at det er et Action-event og altså ikke bare et event. Man skal gerne kunne læse navne på variabler, metoder og klasser, og forstå koden. Dette er med til at koden er mere let-læsbar og nemmere at forstå. Skaber ikke forvirring i et større projekt. 

3. These nested if statements could be combined.

Her kan de to if-statements kombineres, ved brug af && eller || operatoren. 

4. Avoid unused constructor parameters such as 'isCopy'.

Her brokker den sig over en boolean der er kaldt "isCopy". Personlidt syntes jeg at isCopy er en god navngivning. "is" siger at det er en boolen, altså true eller false, og "Copy" beskriver at det har noget med at kopirer noget at gøre. Den syntes jeg personlidt ikke selv skal ændres. PMD er kun en hjælpende hånd mod god kode, den har ikke altid ret. 

5. Avoid unused local variable such as 'currentinput'.

Den brokker sig over en klasse som hedder NeuralInput som der laves et object af der bliver kaldt for "currentInput". Her kunne man i stedet for "currentInput" godt kalde objectet noget i stil med "neutralCurrentImput" eventuelt. 

6. Avoid unused private fields such as 'QUEUE_NAME'.

QUEUE_NAME er et rigtig dårligt navn, dette skal ændres til noget helt andet. 

7. Avoid unused private methods such as 'difference(List,List)'.

Metoder bliver aldrig brugt, hvis dette skal i produktion skal den fjernes, eller i hvert fald udkommenteres. 

- De tre sidste er tre forskellige kommentere. Kommentare i kode, hvis gjort ordenlidt kan fungere rigtig godt. Men som det ses nedenfor, er det kommentare som bør fjernes på et tidspunkt. Hvis man kan undgå at skrive kommentare bør dette gøres. Brug hellere gode navne til klasser, metoder og variable. Man skal gerne kunne gennemskue koden uden at læse andet et dette. IMHO.

8. TODO why not???
9. TODO fix.
10. TODO refactor with forms.

# Opgave 2

- You can again pick something from a project you have done yourself, or just pick something from the GraphQL sample.
- The exercise is to locate a library call which takes up most of the overall execution time for a task.
- If you do not have something of your own, I suggest you look into the the “find droid by name” test.
- Notice, it calls `executor.execute(query).data` - which is the thing we should see where its bottlenecks are hidden.

## Svar

Til at løse denne opgave anvendes NetBeans IDE 8.2 Project Profiler. Jeg har valt at kigge på et af de kald som tager længst tid, nemlig hele 1,3 sekunder! Fordi systemet kører en GUI vil der være en metode der konstant tæller op, nemlig "Java2D Queue Flusher" som er med til at køre GUI'en, den kigger og andre som denne kigger jeg ikke på.

Jeg har i stedet valgt at kigge på følgende: 

![alt text](https://github.com/pwestdk/Real_life_code/blob/master/images/projectprofiler.png)
