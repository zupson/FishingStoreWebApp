# POSTAVLJANJE DOCKERA
## Kreiranje datoteke docker-compose.yml - recept za keranje kontejnera
<img width="1626" height="703" alt="image" src="https://github.com/user-attachments/assets/daa488c0-0eb7-4ad6-a3ae-dc190d5a2109" />

    - Koju sliku (image) da preuzme
    - Koje postavke da koristi
    - Na koji port da sluša

## Pokretanje docker composea
<img width="2390" height="305" alt="image" src="https://github.com/user-attachments/assets/32bb741c-8150-47b2-bd77-55ca773669c5" />

1. Docker čita docker-compose.yml
2. Preuzima PostgreSQL 15 image s interneta (samo prvi put)
3. Kreira container webshop-db
4. Pokreće PostgreSQL unutar njega
5. Kreira bazu webshop

## Provjera pokrenutih kontejnera
 <img width="1815" height="92" alt="image" src="https://github.com/user-attachments/assets/c8b44c1c-1679-4169-9ca0-a95b0c1873da" />

  - Pokreni containere
  docker-compose up -d

  - Zaustavi containere (podaci ostaju!)
  docker-compose down

  - Zaustavi i IZBRIŠI sve podatke (pazi!)
  docker-compose down -v

  - Vidi pokrenute containere
  docker ps

  - Vidi logove baze
  docker logs webshop-db

  - Uđi unutar containera (kao SSH)
  docker exec -it webshop-db psql -U postgres -d webshop

  - Restart containera
  docker-compose restart


## Dodavanje psotgres baze u InteliJ
<img width="1954" height="1194" alt="image" src="https://github.com/user-attachments/assets/ccc9fc93-3a1e-4097-bcf3-f1d41d8b9839" />

## Dodavanje konfiguracije u application.properties
<img width="1117" height="678" alt="image" src="https://github.com/user-attachments/assets/0c439268-c17b-498b-b2a5-85dad1ce5db6" />

### Koraci
1. Pokreni Docker Desktop
2. U terminalu: docker-compose up -d
3. Pokreni Spring Boot aplikaciju u IntelliJ
4. Hibernate automatski sinkronizira tablice
5. Razvijaj aplikaciju
6. Kad završiš: docker-compose down
