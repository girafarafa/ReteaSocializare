Laborator 4 - MAP (Refactorizare Java 8)
Proiect: Retea de socializare

1. INTERFETE FUNCTIONALE FOLOSITE SI UNDE APAR

- Validator<T> (interfata mea, cu @FunctionalInterface):
  Apare in domain.Validation.Validator, in Main.java si in repository-uri. Are o singura 
  metoda abstracta (validate) si metoda default andThen. In Main am scris o regula noua 
  cu lambda (sa inceapa prenumele cu litera mare) si am legat-o de UserValidator cu andThen(), 
  fara sa mai fac o clasa separata.

- Predicate<T>:
  Apare in Repository la metoda default findAll(Predicate<E> filter), in UserCriteria si in 
  .filter() / .anyMatch() din service-uri. In UserCriteria am pus metode statice care intorc 
  Predicate<User> si se pot combina intre ele cu and, or, negate.

- Function<T, R>:
  Apare in RepositoryFactory (pusa intr-un EnumMap ca sa scap de switch cand creez repo-urile) 
  si in operatiile .map(...) din stream-uri si Optional (de exemplu cand transform un obiect 
  Friendship in User sau un User in nume complet).

- Consumer<T>:
  Apare la .forEach(...) si Optional.ifPresent(...) in ConsoleUI si UserService, unde am pus 
  referinte la metode gen System.out::println sau friendshipRepository::delete.

- Supplier<T>:
  Apare in Optional.orElseThrow(() -> new ServiceException(...)) in UserService si 
  FriendshipService, ca sa creeze exceptia doar daca nu gaseste entitatea.

- Comparator<T>:
  Apare in UserService si FriendshipService pentru sortari, folosind Comparator.comparing, 
  thenComparing si reversed, fara sa mai scriu compare de mana.

2. DECIZII LUATE IN PROIECT

1. De ce am pastrat null in Repository in loc sa schimb in Optional<E>:
Am ales ca metodele din interfata Repository (find, save, delete, update) sa returneze 
tot null cand nu gasesc ceva, ca la Lab 3, iar in UserService si FriendshipService le 
impachetez cu Optional.ofNullable(...). Am facut asa ca sa nu modific toate clasele de 
repository existente (InMemoryRepository, AbstractFileRepository, scheletele DbRepository) 
si sa las partea de verificare si aruncare a exceptiilor (orElseThrow, orElse, map) 
doar in Service.

2. De ce am pastrat Template Method (AbstractFileRepository) in loc de Function:
Am pastrat clasa AbstractFileRepository cu subclasele UserFileRepository si 
FriendshipFileRepository, dar am schimbat citirea si scrierea pe Files.lines si Files.write. 
Mi s-a parut mai simplu si mai clar sa ramana fiecare subclasa cu regulile ei de parsare 
(extractEntity si createEntityAsString), pentru ca userii si prieteniile arata diferit in 
fisier, iar AbstractFileRepository se ocupa doar de lucrul efectiv cu fisierul si de prins 
IOException (pe care il arunc mai departe ca RepositoryException). Asa nu am mai incarcat 
RepositoryFactory cu logica de citire din fisier.

3. BUCLE PASTRATE SI DE CE
- ComunityService (parcurgerea grafului cu DFS / backtracking):
Am pastrat buclele clasice in metodele pentru numarul de comunitati si cea mai sociabila 
comunitate. La backtracking pentru cel mai lung drum trebuie sa marchez nodurile vizitate 
cand intru in recursivitate si sa le demarchez cand ma intorc, iar daca incercam sa fac 
asta cu stream-uri iesea un cod foarte greu de citit si de inteles.

4. Gestionarea stergerii prieteniilor la eliminarea unui utilizator
Alegere implementata: ON DELETE CASCADE la nivel de baza de date (pe
cheile straine id1 si id2 din tabela friendships care refera users(id)),
completata de curatarea din UserService pentru compatibilitatea cu
stocarea in memorie/fisiere.

- ConsoleUI.start():
Am lasat bucla while (true) pentru meniul din consola, pentru ca nu parcurge o colectie 
de date, ci doar asteapta comenzi de la tastatura pana cand aleg optiunea 0 (Exit).

