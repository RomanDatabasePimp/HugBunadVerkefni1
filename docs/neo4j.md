# neo4j

The name of neo4j's query language is **Cypher**

## Examples

```cypher
//------------------------------------------------------------------------------------------------------
// Create 3 User nodes, user nodes represent users in the grahp,
// n:User, n táknar nóðu og User er nóðu typa þ.a þetta er User-nóða
// PASSA ! SINCE ÞETTER ERU NODUR ÞU GETUR HAFT DUPLICATE NODUR 
// þu þarft ekki að bæta AI id's noe4j er með automatic ID's fyrir nodur (more on that later)
//------------------------------------------------------------------------------------------------------
CREATE (n:User { name: 'Roman',lasname:'Rumba', Email: 'ror9@hi.is' })
CREATE (n:User { name: 'Vilhelm',lasname:'lunden', Email: 'vilhelml@hi.is' })
CREATE (n:User { name: 'Davið',lasname:'Helgasson', Email: 'dah@hi.is' })

CREATE (n:Gchat { name: 'VeryFirstChat' })
CREATE (n:Ochat { name: 'VeryFirstChat' })

//------------------------------------------------------------------------------------------------------
// fletta upp nodur : MATCH fallið er notað með þvi þú getur skilgreind hvaða
// typu af nodu viltu velja og lika hægt að assigna breytu á þá nodur
// her Noda er assignuð 'n' breytuni og svo til að þá það sem output það þarf að nota RETURN
//------------------------------------------------------------------------------------------------------
MATCH(n:User)  RETURN n
MATCH(n:Gchat) RETURN n
MATCH(n:Ochat) RETURN n
MATCH(a:User),(b:Gchat),(c:Ochat) RETURN a,b,c

//------------------------------------------------------------------------------------------------------
// Skila id's fall fyrir neðan skilar öll id's á user,Gchat,Ochat nodum 
// 'id' er fall i neo4j
//------------------------------------------------------------------------------------------------------
MATCH(n:User)  RETURN id(n)
MATCH(n:Gchat) RETURN id(n)
MATCH(n:Ochat) RETURN id(n)
MATCH(a:User),(b:Gchat),(c:Ochat) RETURN id(a),id(b),id(c)

//------------------------------------------------------------------------------------------------------
// Búa til sambönd milli nóða : það þarf first að fletta up nóðurnar með MATCH og svo nota
// CREATE til að búa til samband milli tveggja nóða t.d búa til vina samband milli Roman og Vilhelms nodur
// NOTICE NEO4J HEFUR BARA DIRECTIONAL RELATIONSHIPS !!!! þ.a stiki þarf að benda á annan hvort 
//-------------------------------------------------------------------------------------------------------
MATCH(a:User),(b:User)                             
WHERE a.name = 'Roman' AND b.name='Vilhelm'
CREATE (a)-[r:FRIENDS]->(b)

MATCH(a:User),(b:User)                             
WHERE a.name = 'Vilhelm' AND b.name='Davið'
CREATE (a)-[r:FRIENDS]->(b)

//--------------------------------------------------------------------------------------------------------
// Leita af vinum Nodunar Vilhelms : Við byrjum að leita af vilhelm Nodu og öllum öðrum User-nodum
// og svo leitum af þeir sem hafa [:FRIENDS] samband við vilhelm.
// NOTICE !!! since þetta er stefnunet og við munum ekki vita alltaf i havaða stefnu er vina sambandið
// er við tökum framm að okkur er sama i hvaða stefnu er vina sambandið  (a)-[:FRIENDS]-(b) ekki (a)-[:FRIENDS]->(b)
//--------------------------------------------------------------------------------------------------------
MATCH (a:User),(b:User)
WHERE a.name ='Vilhelm' AND (a)-[:FRIENDS]-(b)
RETURN b

//--------------------------------------------------------------------------------------------------------
// það er hægt að geyma gögn lika i stikum t.d ef við buúm tiæ samband milli Rómans og Gchat nodu með
// með stika sem táknar að Róman er i chatti og að hann er eigandi á chatti og hann á lika admin réttendi 
//--------------------------------------------------------------------------------------------------------
MATCH (a:Gchat),(b:User)
WHERE b.name = 'Roman' AND a.name = 'VeryFirstChat'
CREATE (b)-[r:GCHAT { isCreator:true, isAdmin: true }]->(a)

MATCH (a:Gchat),(b:User)
WHERE b.name = 'Vilhelm' AND a.name = 'VeryFirstChat'
CREATE (b)-[r:GCHAT { isCreator:false, isAdmin: true }]->(a)

MATCH (a:Gchat),(b:User)
WHERE b.name = 'Davið' AND a.name = 'VeryFirstChat'
CREATE (b)-[r:GCHAT { isCreator:false, isAdmin: false }]->(a)

//--------------------------------------------------------------------------------------------------------
// Leitun af nodum sem hafa samböndum með örðum nodum og þar sem stikar hafa ákveðin gildi
// Leitum af nodu sem bjó til "VeryFirstChat" nodu og svo leitum af öllum adminum i "VeryFirstChat" noduni
// og Svo loks leitum af öllul i "VeryFirstChat" noduni.
// NOTICE !!!! að t.d þessar nodur hafa sambönd á milli sin i graph en þetta kemur ekki upp i Text/Table output
//--------------------------------------------------------------------------------------------------------
MATCH (a:Gchat),(b:User)
WHERE a.name = 'VeryFirstChat' AND (a)-[:GCHAT {isCreator:true}]-(b)
RETURN b

MATCH (a:Gchat),(b:User)
WHERE a.name = 'VeryFirstChat' AND (a)-[:GCHAT {isAdmin:true}]-(b)
RETURN b

MATCH (a:Gchat),(b:User)
WHERE a.name = 'VeryFirstChat' AND (a)-[:GCHAT]-(b)
RETURN b

//--------------------------------------------------------------------------------------------------------
// eyða nodu : þú byrjar first nodu með MATCH og svo notar  DELETE til að eyða henni
// t.d við höfum ekki notað Ochat nodu þ.a við ætlum að eyða henni nuna
//--------------------------------------------------------------------------------------------------------
MATCH(n:Ochat) 
WHERE n.name= 'VeryFirstChat'
DELETE n

//--------------------------------------------------------------------------------------------------------
// eyða samband milli noda : byrjað first að finna samband/önd sem þú vilt eyða og svo nota DELETE
// t.d davið er hent úr chatti 
// NOTICE !!! gerum þetta með smá meira stile 1-liner
//--------------------------------------------------------------------------------------------------------
MATCH (:User {name: 'Davið'})-[r:GCHAT]-(:Gchat{name:'VeryFirstChat'})
DELETE r

// eyðum öllum adminum úr chatti
MATCH (:User)-[r:GCHAT {isCreator:false,isAdmin:true}]-(:Gchat{name:'VeryFirstChat'})
DELETE r
 
```


## Sources

