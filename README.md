Asilane, un "Siri like" libre
=============================

Asinale vous permet d'avoir la liberté d'utiliser un assistant personnel comme vous le souhaitez, sur n'importe quelle plateforme (ou presque).

Installation
------------
Pour lancer Asilane, **exécutez Asilane.jar** ou tapez dans un terminal `java -jar Asilane.jar`.
<br>Ce programme est créé en Java, il est donc **compatible Windows/Mac/Linux**.

Utilisation
-----------
Demandez-lui ce que vous voulez, voici quelques exemples :

* Quelle est la météo à Lille ?
* Envoi un mail à Michael@Vendetta.fr en lui disant qu'il est beau.
* Enregistre ce que je dis : les chaussettes de l'archiduchesse... (Enregistrement dans le presse-papier)
* Va sur Walane.net / Cherche des informations sur Walane (Recherche DuckDuckGo)
* Qu'est ce qu'un chien/chat/ordinateur ? (Wikipédia)
* Montre la vidéo de Bill Gates qui se prend une tarte
* Quel jour sommes-nous ? / quel heure est-il ?
* Et beaucoup d'autres...

Application Android
-------------------
Asilane a été également porté sur Android, [le dépôt GitHub est ici pour plus de détails](https://github.com/walane/Asilane-android).

Licence
-------
**Ce logiciel est libre** selon les termes de [cette licence](https://github.com/walane/Asilane/blob/master/LICENSE).
<br>Vous avez donc la liberté de l'exécuter, de l'étudier, de le modifier et de le partager selon les termes ci-dessus.

Contribuer
----------
**Aidez-moi à construire un "Siri like" libre !** Pour cela, il vous faut connaître un minimum le langage de programmation JAVA. Ouvrez Eclipse et importez directement le projet GIT. Pour comprendre le fonctionnement du logiciel, regardez ci-dessous ;)

<br>
Documentation
-------------

### Architecture générale
La classe mère s'appelle `Asilane`, j'ai mis du temps à trouver un nom original. C'est elle qui se charge d'enregistrer la voix de l'utilisateur lorsque celui-ci clique sur le bouton "Record".
<br>Elle va ensuite exécuter la méthode `closeRecordAndHandleSentence()` qui va se charger du processus de traitement de la phrase :

* La capture audio est envoyée à Google pour la transformer en texte, Asilane s'occupe surtout d'analyser les phrases qui lui ont proposés.
 * La requête de l'utilisateur est ensuite envoyée à la classe `Facade` qui va se charger de nettoyer la phrase (espaces, majuscules, minuscules)
    
     * La `Facade` fait ensuite appel au `ServiceDispatcher` qui répertorie toutes les commandes qui déclenchent les services (par exemple quel est la météo à Lille ?). C'est lui qui va dire à la facade quelle service il faut appaller appeler en fonction de ce qu'a dit l'utilisateur.

 * Une fois que la `Facade`connaît quel service correspond à la phrase elle appelle la méthode `handleSentence()` du service en question qui va retourner la réponse à la question posée par l'utilisateur (par exemple : Quel est la météo à Lille ? -> Il pleut).

* La réponse remonte alors jusqu'à Asilane qui va l'afficher et la dicter par la synthèse vocale de Google (ou celle du téléphone si l'utilisateur utilise l'application Android).

### Créer un `Service`

1. Créez une classe dans le package `com.asilane.service` qui implémente l'interface `IService`, ou copiez la classe `FortyTwoService` qui contient tout ce qu'il faut pour démarrer.

2. Dans la méthode `getCommands()`, remplacez "quel.* est le sens de la vie" par la question que vous voulez (c'est pour aidez le ServiceDispatcher à trouver le service associé à une requête d'un utilisateur). Vous pouvez utilisez des jokers ".*" pour avoir plus de tolérance dans la compréhension des phrases.

3. Dans la méthode `handleService()`, vous devez retournez la réponse à la question posée (`sentence`). Dans `FortyTwoService`, la réponse retournée est toujours 42 quelque soit la question posée comprise dans `getCommands()`.

4. Si vous souhaitez un contrôle plus fin des phrases vous pouvez utilisez la classe `AsilaneUtils` qui vous permet entre autres d'extraire des variables dans une expression régulière (par exemple d'extraire "Lille" dans "Quelle est la météo à Lille ?". Pour en comprendre le fonctionnement, vous pouvez consulter la classe `WebBrowserService` pour utiliser ça dans votre `Service` en gardant la même architecture ci-possible.

5. Pour finir la fonction `handleRecoveryService()` vous permet de faire la même chose que la méthode `handleSentence()` sauf que la première est appellée lorsque la phrase n'a pas été comprise par le `ServiceDispatcher`. Celui-ci appelle-donc le dernier service qui a été exécuté et envoi la phrase incomprise. Ceci vous permet d'avoir un dialogue plus confortable avec Asilane, vous pouvez par exemple lui demandez "Qu'est-ce qu'un chien ?" puis, "et un chat ?".

Le dernier point fonctionne grâce à un arbre de recherche disponible dans la méthode `handleSentence()` qui enregistre tout ce qui est dit, quelles sont les derniers services appellés, leur réponse, etc. Pour plus d'informations, consultez les classes `HistoryTree` et `HistoryNode`.

### Tutoriel vidéo
Un ancien [tutoriel vidéo](https://www.youtube.com/watch?v=h3dgMjBSUpA) est disponible pour comprendre un peu plus en détails le fonctionnement d'Asilane.
