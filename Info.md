# 플러그인 이벤트 감지내용

- 헌터끼리는 때릴 수 없고 헌터와 러너사이의 PVP가능


- 헌터는 엔더 포탈, 드래곤 알 상호작용 불가


- 헌터는 드래곤 알 줍기 불가

---

- administrator에 등록되지 않은 사람들은 명령어 불가. (채팅은 전체 불가이나 명령어로 채팅 가능.)


- administrator에 등록된 사람은 인원수 초과 접속 가능


- 침대는 Overworld만, 리스폰 정박기는 Nether만 가능


- **runner에 등록된 사람이 새로운 발전과제 달성 시 최대 플레이어 접속자 1명 추가, config 자동저장.**

---

## 관리자 명령어

- /advc maxPlayers (서버 최대 플레이어 접속자를 보여줌. 뒤에 정수 인수를 붙히게 되면 직접 설정 가능함.)


    Example: /advc maxPlayers 12


- /advc administrator (관리자 UUID 설정을 보여줌. 이벤트와 관련된 내용쪽에 영향을 끼침. 뒤에 문자열인수를 붙히게 되면 직접 설정 가능함. **단 내용은 무조건 UUID여야 작동함.**)
  

    Example: /advc administrator 389c4c9b-6342-42fc-beb3-922a7d7a72f9 5082c832-7f7c-4b04-b0c7-2825062b7638 762dea11-9c45-4b18-95fc-a86aab3b39ee 63e8e8a6-4104-4abf-811b-2ed277a02738 ad524e9e-acf5-4977-9c12-938212663361
 

- /advc runner (플러그인 플레이어, 즉 러너의 UUID 설정을 보여줌. 이벤트와 관련된 내용쪽에 영향을 끼침. 뒤에 문자열인수를 붙히게 되면 직접 설정 가능함. **단 내용은 무조건 UUID여야 작동함.**)
  

    Example: /advc runner 389c4c9b-6342-42fc-beb3-922a7d7a72f9

**config가 건드려지는 설정은 리로딩이 필요 할 수 있음.**