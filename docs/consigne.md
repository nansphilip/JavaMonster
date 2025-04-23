# TD3 : Java, cas pratique
**V3.0.0**

> Cette œuvre est mise à disposition selon les termes de la licence Creative Commons Attribution – Pas d'Utilisation Commerciale – Partage à l'Identique 3.0 non transposé.  
> Document en ligne : www.mickael-martin-nevot.com

---

## 1. Généralités

Écrivez l’application ci-dessous en Java et en respectant la norme de programmation donnée en cours puis testez-les.

Vous devez créer une application de simulation d’hôpital fantastique pour créatures, nommé **Fantasy Hospital**, ou sa traduction dans une autre langue.

Ci-dessous, vous trouverez la spécification minimale, partiellement incomplète, de l’application.  
À vous de créer des classes, des variables d'instance, des méthodes ou des interfaces supplémentaires dès que cela semble nécessaire.  
À vous également de vous doter du savoir encyclopédique nécessaire pour rendre la modélisation la plus crédible possible.

Au cas où vous en ayez l’utilité pour l’application en elle-même ou pour sa documentation, l’archive compressée TD3 : Java, cas pratique, ressources contient des visuels en relation avec le sujet. Notamment pour le même usage, vous pouvez aussi créer les vôtres.

---

## 2. Les créatures

Au minimum, l’application doit permettre de gérer :
- des elfes
- des nains
- des orques
- des homme-bêtes
- des zombies
- des vampires
- des lycanthropes (ou loups-garous)
- des reptiliens

**Toutes les créatures doivent posséder les caractéristiques suivantes :**
- un nom complet
- un sexe
- un poids
- une taille
- un âge
- un indicateur de moral
- une liste de maladies

**Toutes les créatures doivent pouvoir :**
- attendre (ce qui diminue le moral)
- hurler (lorsque leur moral est au plus bas)
- s'emporter (en remplacement d'hurlements consécutifs)
- tomber malade (ce qui ajoute une maladie à la liste de maladies)
- être soigné (ce qui traite une maladie et redonne du moral)
- trépasser (s'il devient trop malade)

**Comportements spécifiques :**
- En trépassant, les elfes et les vampires doivent pouvoir démoraliser une partie des créatures de leur service médical.
- Les créatures bestiales (orques, homme-bêtes, lycanthropes et vampires) doivent pouvoir contaminer (en transmettant une de leur maladie à une autre créature de leur service médical).
- Les créatures mortes-vivantes (zombies et vampires) doivent pouvoir régénérer directement après avoir trépassé.
- Les habitants du triage (orques, homme-bêtes, zombies et lycanthropes) attendent plus patiemment dès lors qu'ils le font avec au moins un autre représentant de leur espèce.
- Les clients VIP prioritaires (elfes, nains, vampires et reptiliens) voient leur moral évoluer au plus bas dès qu'ils doivent attendre trop longtemps.
- Une créature qui s'emporte a de bonnes chances de contaminer une autre créature.

---

## 3. Les maladies

En dehors des exceptions décrites ci-après, toutes les créatures ont une chance de contracter, au minimum, une des maladies suivantes :
- Maladie débilitante chronique (MDC)
- Syndrome fear of missing out (FOMO)
- Dépendance aux réseaux sociaux (DRS)
- Porphyrie érythropoïétique congénitale (PEC)
- Zoopathie paraphrénique lycanthropique (ZPL)
- <Nom de maladie à définir> (NDMAD ou BG, au choix)

**Toutes les maladies doivent posséder les caractéristiques suivantes :**
- un nom complet
- un nom abrégé
- un niveau actuel
- un niveau maximum (avant trépas)

**Toutes les maladies doivent pouvoir :**
- diminuer, augmenter ou changer leur niveau actuel
- déterminer si le niveau de la maladie est léthal

---

## 4. Les services médicaux

Chaque service médical doit pouvoir contenir plusieurs créatures.  
Dans un service médical, toutes les créatures doivent être du même type (les orques avec les orques, les zombies avec les zombies, etc.).  
Cependant, il n'est pas possible de mettre n'importe quel type de créature dans n'importe quel service médical.

**Un service médical doit posséder les caractéristiques suivantes :**
- un nom
- une superficie
- le nombre maximum de créatures qu'il peut contenir
- le nombre de créatures présentes
- les créatures présentes
- un budget (ayant au moins pour valeurs : « inexistant », « médiocre », « insuffisant » et « faible »)

**Il doit permettre :**
- d'afficher ses caractéristiques ainsi que celles des créatures qu'il contient
- d'ajouter et d'enlever des créatures
- de soigner les créatures qu'il contient
- de réviser le budget

**Catégories spécifiques :**
- **Centre de quarantaine** :  
  - Ne peut contenir que des créatures contagieuses  
  - Possède une caractéristique supplémentaire : l'isolation  
  - La révision du budget inclut la prise en compte de l'isolation en plus de la révision classique
- **Crypte** :  
  - Ne peut contenir que des créatures régénérantes  
  - Possède deux caractéristiques supplémentaires : le niveau de ventilation et la température  
  - La révision du budget nécessite la vérification de ces deux caractéristiques supplémentaires à la place de la révision classique

---

## 5. Les médecins

Tous les médecins sont d'un type de créature donné, ne peuvent pas tomber malade et doivent posséder les caractéristiques suivantes :
- un nom
- un sexe
- un âge

**Ils doivent pouvoir :**
- examiner un service médical (en affichant les caractéristiques du service médical ainsi que la liste des créatures)
- soigner les créatures d'un service médical
- réviser le budget d'un service médical
- transférer une créature d'un service médical à un autre

À l'aide d'un menu, vous devez permettre à l'utilisateur de pouvoir diriger les médecins.

---

## 6. L'hôpital fantastique

Un hôpital fantastique doit posséder les caractéristiques suivantes :
- un nom
- un nombre maximal de services médicaux
- les services médicaux existants
- les médecins

**Il doit permettre :**
- d'afficher le nombre de créatures présentes dans l'hôpital fantastique
- d'afficher les créatures de tous les services médicaux

Il doit en plus avoir la méthode principale de l'application (point d'entrée de la simulation) qui est chargée de modéliser l'aspect temporel de la gestion de l'hôpital fantastique.  
À intervalle régulier, cette méthode doit :
- modifier aléatoirement l'état de certaines créatures (les rendre malades, faire évoluer leurs maladies, faire évoluer leur moral, etc.)
- modifier aléatoirement l'état de certains services médicaux (leur budget, leur isolation, leur température, etc.)
- passer la main à un médecin (et donc à l'utilisateur) pour qu'il s'occupe de l'hôpital fantastique (son nombre d'actions par intervalle de temps devant être limité)

---

## 7. Simulation

Équilibrez la simulation pour la rendre ludique.