# 스프링 DB - 1편

## H2 데이터베이스 설정

> 해당 프로젝트에서 어떤 버전의 H2 데이터베이스를 사용하는지 확인한다.

<img width="296" alt="스크린샷 2022-04-16 오후 5 37 25" src="https://user-images.githubusercontent.com/23515771/163668391-4c8c194e-755c-4789-93e7-d5a8aaed2269.png">

해당 프로젝트에서 사용하는 H2 데이터베이스가 `1.4.200` 버전임을 확인하고, https://www.h2database.com/html/main.html 에
접속해서 `All Downloads -> Archive Downloads` 로 들어간다.

<img width="376" alt="스크린샷 2022-04-16 오후 5 39 17" src="https://user-images.githubusercontent.com/23515771/163668447-50cd2181-598d-48f4-b9d4-e10e490f41c2.png">

`1.4.200` 버전의 `Platform-Independent Zip` 을 다운로드 하고, 압축을 해제한다.

> H2 데이터베이스 실행하기

`h2/bin` 으로 디렉토리를 이동하여, `chmod 755 h2.sh` 명령을 통해 실행 권한을 허용한다.

<img width="486" alt="스크린샷 2022-04-16 오후 5 42 01" src="https://user-images.githubusercontent.com/23515771/163668509-f75c002c-836b-4be6-a2ec-807d0ca11aea.png">

그리고 나서 `./h2.sh` 를 실행한다.

<img width="355" alt="스크린샷 2022-04-16 오후 5 42 36" src="https://user-images.githubusercontent.com/23515771/163668530-784adca8-6f8b-4cf9-8268-fe0423085f22.png">

> H2 데이터베이스 접속

Host를 반드시 `localhost:8082` 로 변경해서 접속해야 한다.

> H2 데이터베이스 연결하기

최초 한번은 파일로 접근하는 방식으로 연결을 해야한다.

<img width="464" alt="스크린샷 2022-04-16 오후 5 44 51" src="https://user-images.githubusercontent.com/23515771/163668590-fc822574-b484-4c3e-bc23-8b5cd57d6122.png">

그리고 나서 `~` 경로에 `test.mv.db` 가 생성되었는지를 꼭 확인해야 한다.

<img width="482" alt="스크린샷 2022-04-16 오후 5 46 59" src="https://user-images.githubusercontent.com/23515771/163668624-e76d62de-635e-4de0-a8e7-818064346016.png">

그리고 이후 부터는 `jdbc:h2:tcp://localhost/~/test` 로 접속하면 된다.

<img width="474" alt="스크린샷 2022-04-16 오후 5 49 48" src="https://user-images.githubusercontent.com/23515771/163668682-7866b74b-e115-4e5f-b275-3011d1859008.png">

## JDBC 표준 인터페이스

JDBC(Java Database Connectivity)는 자바에서 데이터베이스에 접속할 수 있도록 하는 자바 API다. 대표적으로 다음 3가지 기능을 표준 인터페이스로 정의해서 제공한다.

- `java.sql.Connection` - 연결
- `java.sql.Statement` - SQL을 담은 내용
- `java.sql.ResultSet` - SQL 요청 응답

이 JDBC 인터페이스를 각각의 DB 벤더에서 자신의 DB에 맞도록 구현해서 라이브러리를 제공하는데, 이것을 **`JDBC 드라이버`** 라고 한다.

<img width="1704" alt="image" src="https://user-images.githubusercontent.com/23515771/163669304-058ccd6a-d793-4b49-baec-46dfcc6e7f40.png">

`애플리케이션 로직` 에서는 `JDBC 표준 인터페이스` 를 그대로 사용하면 되고, `드라이버` 구현체만 바꾸면 된다. (개인적인 생각으로 **`전략 패턴`** 을 기반으로 구현한 것 같다.)

## JDBC를 편리하게 사용하는 다양한 기술들

### SQL Mapper

> 장점

- SQL 응답 결과를 객체로 편리하게 변환해준다.
- JDBC의 반복 코드를 제거해준다.

> 단점

- 개발자가 직접 SQL을 작성해야 한다.

> 대표 기술

- Spring JdbcTemplate, MyBatis

### ORM

객체를 관계형 데이터베이스 테이블과 매핑해주는 기술이다. 이 기술 덕분에 개발자는 SQL을 직접 작성하지 않아도 된다.

> 대표 기술

- JPA, Hibernate, Eclipse Link

## JDBC DriverManager 연결 이해

### DriverManager 커넥션 요청 흐름

<img width="832" alt="스크린샷 2022-04-16 오후 7 50 18" src="https://user-images.githubusercontent.com/23515771/163672129-c9a5933a-3e2a-4679-8346-a957af8e9662.png">

드라이버는 `jdbc:h2:tcp://localhost/~/test` 의 URL을 통해 내가 처리할 수 있는 연결인지 `H2, MySQL, Oracle` 를 차례대로 확인한다.

- `jdbc:h2` 는 H2 데이터베이스에 접근하기 위한 규칙
