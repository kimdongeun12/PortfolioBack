SET NAMES utf8mb4;
SET @old_safe_updates = @@SQL_SAFE_UPDATES;
SET SQL_SAFE_UPDATES = 0;
START TRANSACTION;

UPDATE experience SET
    company_name = '(주) 클러쉬',
    former_name = '디씨온',
    department = 'KB개발담당',
    job_title = NULL,
    role = '국민지갑 플랫폼의 프론트엔드 업무를 리딩하며 구조 설계, 공통화, 서비스 개발과 운영을 경험했습니다. SSO 인증, GLN 해외결제, 메인 UI 고도화, 기차예매 개선 등 다양한 금융 서비스를 담당했고, 합병 이후에도 플랫폼 개발과 운영, 재사용 구조 개선을 이어가고 있습니다.',
    position = '선임연구원(대리)',
    responsibilities = '프론트엔드 구조 개선 및 표준화
플랫폼 서비스 기능 개발 및 운영·유지보수 대응
공통 컴포넌트 설계·최적화 및 재사용 구조 정비
계열사 연계 인증·로그인 공통화 및 SSO 연동
웹·앱 접근성 기준 대응 및 인증 취득 지원
고객 협의 기반 요구사항 조율 및 로직 리팩토링',
    achievements = '공통 컴포넌트를 국민지갑 28개 서비스에 적용해 신규 화면 개발 공수를 1MM에서 0.5MM으로 약 50% 절감
KB Pay를 시작으로 KB 계열사 앱 6종에 국민지갑 SSO 로그인 연계
GLN 해외결제 개편 후 결제 화면 페이지 이탈률 약 15% 감소
기차예매 선물하기 도입 후 승차권 페이지 사용자 유입률 약 20% 증가',
    start_date = '2023-06-21',
    end_date = NULL
WHERE company_name IN ('(주) 클러쉬', '클러쉬') AND is_deleted = 0;
INSERT INTO experience (company_name, former_name, department, job_title, role, position, responsibilities, achievements, start_date, end_date)
SELECT '(주) 클러쉬', '디씨온', 'KB개발담당', NULL, '국민지갑 플랫폼의 프론트엔드 업무를 리딩하며 구조 설계, 공통화, 서비스 개발과 운영을 경험했습니다. SSO 인증, GLN 해외결제, 메인 UI 고도화, 기차예매 개선 등 다양한 금융 서비스를 담당했고, 합병 이후에도 플랫폼 개발과 운영, 재사용 구조 개선을 이어가고 있습니다.', '선임연구원(대리)', '프론트엔드 구조 개선 및 표준화
플랫폼 서비스 기능 개발 및 운영·유지보수 대응
공통 컴포넌트 설계·최적화 및 재사용 구조 정비
계열사 연계 인증·로그인 공통화 및 SSO 연동
웹·앱 접근성 기준 대응 및 인증 취득 지원
고객 협의 기반 요구사항 조율 및 로직 리팩토링', '공통 컴포넌트를 국민지갑 28개 서비스에 적용해 신규 화면 개발 공수를 1MM에서 0.5MM으로 약 50% 절감
KB Pay를 시작으로 KB 계열사 앱 6종에 국민지갑 SSO 로그인 연계
GLN 해외결제 개편 후 결제 화면 페이지 이탈률 약 15% 감소
기차예매 선물하기 도입 후 승차권 페이지 사용자 유입률 약 20% 증가', '2023-06-21', NULL
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM experience WHERE company_name = '(주) 클러쉬' AND is_deleted = 0);
SET @exp_clush = (SELECT id FROM experience WHERE company_name = '(주) 클러쉬' AND is_deleted = 0 ORDER BY id LIMIT 1);

UPDATE experience SET
    company_name = '(주) 엠아이티마스',
    former_name = NULL,
    department = NULL,
    job_title = NULL,
    role = 'KB국민은행 국민지갑 내차등록증 구축에 참여해 차량 등록 및 검사 관련 화면을 개발하고, 차량 정보 연계와 공통 UI·에러 처리 구조를 구성했습니다.',
    position = '프리랜서',
    responsibilities = '프론트엔드 개발 및 공통 컴포넌트 설계
KB 내부 EAI·외부 기관 데이터 연계 화면 개발
에러 처리 공통 모듈 및 데이터 포매팅 표준화
스프린트 기반 고객 요구사항 반영',
    achievements = NULL,
    start_date = '2023-03-01',
    end_date = '2023-04-30'
WHERE company_name IN ('(주) 엠아이티마스', '엠아이티마스') AND is_deleted = 0;
INSERT INTO experience (company_name, former_name, department, job_title, role, position, responsibilities, achievements, start_date, end_date)
SELECT '(주) 엠아이티마스', NULL, NULL, NULL, 'KB국민은행 국민지갑 내차등록증 구축에 참여해 차량 등록 및 검사 관련 화면을 개발하고, 차량 정보 연계와 공통 UI·에러 처리 구조를 구성했습니다.', '프리랜서', '프론트엔드 개발 및 공통 컴포넌트 설계
KB 내부 EAI·외부 기관 데이터 연계 화면 개발
에러 처리 공통 모듈 및 데이터 포매팅 표준화
스프린트 기반 고객 요구사항 반영', NULL, '2023-03-01', '2023-04-30'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM experience WHERE company_name = '(주) 엠아이티마스' AND is_deleted = 0);
SET @exp_mitmas = (SELECT id FROM experience WHERE company_name = '(주) 엠아이티마스' AND is_deleted = 0 ORDER BY id LIMIT 1);

UPDATE experience SET
    company_name = '(주) 인스팟',
    former_name = NULL,
    department = '크리에이티브 그룹 UX팀',
    job_title = NULL,
    role = 'SK넥실리스·현대바이오랜드 등 기업 홈페이지 리뉴얼과 금융 플랫폼 구축에 참여하고, 다수 웹사이트의 운영 및 유지보수를 담당했습니다.',
    position = '사원',
    responsibilities = '플랫폼 신규 개발 및 기존 서비스 유지보수
금융·공공 서비스 프론트엔드 화면 구축
웹표준·반응형·크로스브라우징 기반 퍼블리싱
공통 스타일·컴포넌트 적용을 통한 화면 구조 정비
웹접근성 기준 대응 및 인증 취득 지원',
    achievements = 'The-K저축은행 웹사이트 웹접근성 개선 작업으로 인증 취득에 기여',
    start_date = '2020-12-01',
    end_date = '2022-08-31'
WHERE company_name IN ('(주) 인스팟', '인스팟') AND is_deleted = 0;
INSERT INTO experience (company_name, former_name, department, job_title, role, position, responsibilities, achievements, start_date, end_date)
SELECT '(주) 인스팟', NULL, '크리에이티브 그룹 UX팀', NULL, 'SK넥실리스·현대바이오랜드 등 기업 홈페이지 리뉴얼과 금융 플랫폼 구축에 참여하고, 다수 웹사이트의 운영 및 유지보수를 담당했습니다.', '사원', '플랫폼 신규 개발 및 기존 서비스 유지보수
금융·공공 서비스 프론트엔드 화면 구축
웹표준·반응형·크로스브라우징 기반 퍼블리싱
공통 스타일·컴포넌트 적용을 통한 화면 구조 정비
웹접근성 기준 대응 및 인증 취득 지원', 'The-K저축은행 웹사이트 웹접근성 개선 작업으로 인증 취득에 기여', '2020-12-01', '2022-08-31'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM experience WHERE company_name = '(주) 인스팟' AND is_deleted = 0);
SET @exp_inspot = (SELECT id FROM experience WHERE company_name = '(주) 인스팟' AND is_deleted = 0 ORDER BY id LIMIT 1);

INSERT IGNORE INTO tech_stack (name) VALUES
    ('Vue'),
    ('Vuex'),
    ('Vuetify'),
    ('TypeScript'),
    ('Scss'),
    ('Git'),
    ('Jenkins'),
    ('Jira'),
    ('HTML5'),
    ('CSS3'),
    ('Javascript'),
    ('JSP'),
    ('starRoot'),
    ('Next'),
    ('React'),
    ('Ant Design'),
    ('MDX'),
    ('Spring'),
    ('MySQL'),
    ('Flyway'),
    ('AWS'),
    ('Webpack'),
    ('Figma');

UPDATE project SET
    experience_id = @exp_clush,
    title = 'KB국민은행 스타뱅킹 국민지갑 상시 개발 대응',
    summary = '국민지갑의 신규 기능 개발과 운영 대응을 담당하며, 공통 컴포넌트와 개발 표준을 정비하고 계열사 인증 연계 및 결제·예매 서비스를 개발했습니다.',
    role = '프론트엔드 공통 구조 설계 및 개발 표준 정비, KB 계열사 연계, 회원·서비스 개발',
    responsibilities = '모달·팝업·입력 폼 공통 UI를 설계해 국민지갑 28개 서비스에 적용·운영
KB Pay 등 KB 계열사 앱 6종에 국민지갑 SSO 로그인 연계',
    content = '## 공통 컴포넌트 설계 및 개발 표준 정비

- 모달·팝업·입력 폼 등 공통 UI 컴포넌트를 설계하고 국민지갑 내 28개 서비스에 적용·운영했습니다.
- props·slot·emit 사용 규칙을 정의하고, 서비스별 요구사항을 옵션과 슬롯 구성으로 반영할 수 있도록 재사용 구조를 정비했습니다.
- Vuetify 컴포넌트를 KB 디자인 가이드에 맞게 래핑해 화면 간 스타일과 동작을 일관되게 유지했습니다.
- 팝업 호출·상태 관리와 입력 폼의 유효성 검사·에러 메시지 처리를 공통화해 화면별 중복 로직을 줄였습니다.
- 포커스 이동·키보드 조작·스크린리더 대응을 공통 컴포넌트에 반영하고, 사용 가이드를 문서화해 신규 투입 인력이 동일한 기준으로 개발하도록 지원했습니다.

<Result>공통 컴포넌트 재사용을 통해 신규 화면 개발 공수를 1MM에서 0.5MM으로 약 50% 절감했습니다.</Result>

## 국민지갑 메인 UI 개편

- 기획·디자인 담당자와 메인 화면 구조 및 서비스 노출 우선순위를 협의하고 UI 개편을 주도했습니다.
- 진입 시 일괄 호출하던 서비스별 API를 영역 단위로 분리하고, 초기 화면에 필요한 영역을 우선 조회하도록 변경했습니다.
- 서비스 카드·배너 등 메인 구성 요소를 공통 컴포넌트로 분리해 신규 서비스 영역을 추가하기 쉬운 구조로 정비했습니다.
- 스켈레톤 UI를 적용해 데이터 로딩 중에도 화면 구조가 유지되도록 개선했습니다.

<Result>초기 진입 시 불필요한 API 호출과 렌더링 부담을 줄이고, 메인 화면 구성 요소의 재사용성을 높였습니다.</Result>

## KB 계열사 SSO 인증 연계

- 국민지갑의 첫 계열사 SSO 연계인 KB Pay 연동을 담당하고, 계열사 담당자와 인증 흐름 및 앱·웹 인터페이스를 협의했습니다.
- 계열사 제공 스크립트로 앱 기능을 호출하고, 앱에서 전달받은 로그인 토큰을 KB 표준 SSO 인증 흐름에 연계하는 프론트엔드 로직을 구현했습니다.
- KB Pay 연계 시 정립한 방식을 바탕으로 리브똑똑, KB손해보험, KB증권 M-able, KB키위뱅킹, 리브넥스트까지 연계를 확장했습니다.
- 계열사별 컨트롤러를 구성해 서로 다른 네이티브 연동 코드를 분리하고, 각 앱에서 제공하는 기능을 정리했습니다.
- 파일 첨부·카메라 및 사진 접근·새 창 열기 등 하이브리드 웹앱 구동에 필요한 앱 설정 항목을 적용 가이드로 문서화했습니다.

<Result>KB 계열사 앱 6종에 국민지갑 SSO 로그인을 연계하고, 후속 연계에 재사용할 수 있는 컨트롤러 구조와 적용 절차를 마련했습니다.</Result>

## GLN 해외결제 UI/UX 개선 및 결제 로직 리팩토링

- CPM·MPM 분기와 QR 팝업 중심으로 구성된 복잡한 결제 흐름을 개선했습니다.
- 결제 방식 선택을 스위치 UI로 통합하고, 선택 상태에 따라 화면과 결제 흐름을 제어하도록 리팩토링했습니다.
- QR 팝업을 메인 페이지 내 컴포넌트로 통합해 렌더링 계층을 단순화하고, 카드형 UI를 공통 컴포넌트로 구성했습니다.
- QR 스캔·코드 추출·백엔드 전달·응답 데이터 가공·결제 결과 반영으로 이어지는 프론트엔드 처리 흐름을 정리했습니다.
- 잔액 부족 시 충전 절차로 연결되도록 결제 흐름을 개선하고, 불필요한 중복 호출과 예외 처리 구조를 정비했습니다.

<Result>개편 전후 비교 결과, 결제 화면 페이지 이탈률이 약 15% 감소했습니다. (넷스루 DataStory 집계)</Result>

## 기차예매 선물하기 기능 개발

- KTX·SRT 승차권을 예매하는 국민지갑 기차예매 서비스에 선물하기 기능을 개발했습니다.
- 기존 비회원 구매 기반의 예매 구조를 확장하기 위해 국민지갑 고유 식별자(ID)로 승차권을 매핑하는 연계 방식을 설계했습니다.
- 승차권 매핑 방식을 프론트엔드·백엔드 인터페이스에 반영하고, 선물하기 화면과 프론트엔드 로직을 구현했습니다.
- 고객 피드백을 반영해 선물하기 UI/UX와 기능 요구사항을 조율했습니다.

<Result>선물하기 기능 도입 후 승차권 페이지의 사용자 유입률이 기존 대비 약 20% 늘었습니다.</Result>

## Vue 3 마이그레이션 <Badge>진행 중</Badge>

- Vue 2.6 기반 서비스를 Vue 3.5, Vuex 4, Vue Router 4, Vuetify 3, Vite 환경으로 전환하고 있습니다.
- Webpack 4에서 Vite로 빌드 환경을 전환하고 애플리케이션 구동을 확인했습니다.
- 기존 vue-class-component 기반 컴포넌트를 vue-facing-decorator 기반 클래스 문법으로 전환하고 있습니다.
- 마이그레이션 도구의 처리 과정을 분석·사전 점검(dry run)·변환·검증 단계로 나누고, 단계별 리포트를 남기도록 설계했습니다.

## 협업 및 운영

- 주간 스크럼과 고객사 협의를 통해 요구사항 및 개발 우선순위를 조율했습니다.
- 운영 중 발견한 중복 코드와 복잡한 서비스 로직을 지속적으로 리팩토링했습니다.
',
    type = 'COMPANY',
    thumbnail_url = NULL,
    is_pinned = 1,
    sort_order = 1,
    start_date = '2023-06-21',
    end_date = NULL
WHERE title = 'KB국민은행 스타뱅킹 국민지갑 상시 개발 대응' AND is_deleted = 0;
INSERT INTO project (experience_id, title, summary, role, responsibilities, content, type, thumbnail_url, is_pinned, sort_order, start_date, end_date, created_at, updated_at)
SELECT @exp_clush, 'KB국민은행 스타뱅킹 국민지갑 상시 개발 대응', '국민지갑의 신규 기능 개발과 운영 대응을 담당하며, 공통 컴포넌트와 개발 표준을 정비하고 계열사 인증 연계 및 결제·예매 서비스를 개발했습니다.', '프론트엔드 공통 구조 설계 및 개발 표준 정비, KB 계열사 연계, 회원·서비스 개발', '모달·팝업·입력 폼 공통 UI를 설계해 국민지갑 28개 서비스에 적용·운영
KB Pay 등 KB 계열사 앱 6종에 국민지갑 SSO 로그인 연계', '## 공통 컴포넌트 설계 및 개발 표준 정비

- 모달·팝업·입력 폼 등 공통 UI 컴포넌트를 설계하고 국민지갑 내 28개 서비스에 적용·운영했습니다.
- props·slot·emit 사용 규칙을 정의하고, 서비스별 요구사항을 옵션과 슬롯 구성으로 반영할 수 있도록 재사용 구조를 정비했습니다.
- Vuetify 컴포넌트를 KB 디자인 가이드에 맞게 래핑해 화면 간 스타일과 동작을 일관되게 유지했습니다.
- 팝업 호출·상태 관리와 입력 폼의 유효성 검사·에러 메시지 처리를 공통화해 화면별 중복 로직을 줄였습니다.
- 포커스 이동·키보드 조작·스크린리더 대응을 공통 컴포넌트에 반영하고, 사용 가이드를 문서화해 신규 투입 인력이 동일한 기준으로 개발하도록 지원했습니다.

<Result>공통 컴포넌트 재사용을 통해 신규 화면 개발 공수를 1MM에서 0.5MM으로 약 50% 절감했습니다.</Result>

## 국민지갑 메인 UI 개편

- 기획·디자인 담당자와 메인 화면 구조 및 서비스 노출 우선순위를 협의하고 UI 개편을 주도했습니다.
- 진입 시 일괄 호출하던 서비스별 API를 영역 단위로 분리하고, 초기 화면에 필요한 영역을 우선 조회하도록 변경했습니다.
- 서비스 카드·배너 등 메인 구성 요소를 공통 컴포넌트로 분리해 신규 서비스 영역을 추가하기 쉬운 구조로 정비했습니다.
- 스켈레톤 UI를 적용해 데이터 로딩 중에도 화면 구조가 유지되도록 개선했습니다.

<Result>초기 진입 시 불필요한 API 호출과 렌더링 부담을 줄이고, 메인 화면 구성 요소의 재사용성을 높였습니다.</Result>

## KB 계열사 SSO 인증 연계

- 국민지갑의 첫 계열사 SSO 연계인 KB Pay 연동을 담당하고, 계열사 담당자와 인증 흐름 및 앱·웹 인터페이스를 협의했습니다.
- 계열사 제공 스크립트로 앱 기능을 호출하고, 앱에서 전달받은 로그인 토큰을 KB 표준 SSO 인증 흐름에 연계하는 프론트엔드 로직을 구현했습니다.
- KB Pay 연계 시 정립한 방식을 바탕으로 리브똑똑, KB손해보험, KB증권 M-able, KB키위뱅킹, 리브넥스트까지 연계를 확장했습니다.
- 계열사별 컨트롤러를 구성해 서로 다른 네이티브 연동 코드를 분리하고, 각 앱에서 제공하는 기능을 정리했습니다.
- 파일 첨부·카메라 및 사진 접근·새 창 열기 등 하이브리드 웹앱 구동에 필요한 앱 설정 항목을 적용 가이드로 문서화했습니다.

<Result>KB 계열사 앱 6종에 국민지갑 SSO 로그인을 연계하고, 후속 연계에 재사용할 수 있는 컨트롤러 구조와 적용 절차를 마련했습니다.</Result>

## GLN 해외결제 UI/UX 개선 및 결제 로직 리팩토링

- CPM·MPM 분기와 QR 팝업 중심으로 구성된 복잡한 결제 흐름을 개선했습니다.
- 결제 방식 선택을 스위치 UI로 통합하고, 선택 상태에 따라 화면과 결제 흐름을 제어하도록 리팩토링했습니다.
- QR 팝업을 메인 페이지 내 컴포넌트로 통합해 렌더링 계층을 단순화하고, 카드형 UI를 공통 컴포넌트로 구성했습니다.
- QR 스캔·코드 추출·백엔드 전달·응답 데이터 가공·결제 결과 반영으로 이어지는 프론트엔드 처리 흐름을 정리했습니다.
- 잔액 부족 시 충전 절차로 연결되도록 결제 흐름을 개선하고, 불필요한 중복 호출과 예외 처리 구조를 정비했습니다.

<Result>개편 전후 비교 결과, 결제 화면 페이지 이탈률이 약 15% 감소했습니다. (넷스루 DataStory 집계)</Result>

## 기차예매 선물하기 기능 개발

- KTX·SRT 승차권을 예매하는 국민지갑 기차예매 서비스에 선물하기 기능을 개발했습니다.
- 기존 비회원 구매 기반의 예매 구조를 확장하기 위해 국민지갑 고유 식별자(ID)로 승차권을 매핑하는 연계 방식을 설계했습니다.
- 승차권 매핑 방식을 프론트엔드·백엔드 인터페이스에 반영하고, 선물하기 화면과 프론트엔드 로직을 구현했습니다.
- 고객 피드백을 반영해 선물하기 UI/UX와 기능 요구사항을 조율했습니다.

<Result>선물하기 기능 도입 후 승차권 페이지의 사용자 유입률이 기존 대비 약 20% 늘었습니다.</Result>

## Vue 3 마이그레이션 <Badge>진행 중</Badge>

- Vue 2.6 기반 서비스를 Vue 3.5, Vuex 4, Vue Router 4, Vuetify 3, Vite 환경으로 전환하고 있습니다.
- Webpack 4에서 Vite로 빌드 환경을 전환하고 애플리케이션 구동을 확인했습니다.
- 기존 vue-class-component 기반 컴포넌트를 vue-facing-decorator 기반 클래스 문법으로 전환하고 있습니다.
- 마이그레이션 도구의 처리 과정을 분석·사전 점검(dry run)·변환·검증 단계로 나누고, 단계별 리포트를 남기도록 설계했습니다.

## 협업 및 운영

- 주간 스크럼과 고객사 협의를 통해 요구사항 및 개발 우선순위를 조율했습니다.
- 운영 중 발견한 중복 코드와 복잡한 서비스 로직을 지속적으로 리팩토링했습니다.
', 'COMPANY', NULL, 1, 1, '2023-06-21', NULL, NOW(6), NOW(6)
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM project WHERE title = 'KB국민은행 스타뱅킹 국민지갑 상시 개발 대응' AND is_deleted = 0);
SET @prj = (SELECT id FROM project WHERE title = 'KB국민은행 스타뱅킹 국민지갑 상시 개발 대응' AND is_deleted = 0 ORDER BY id LIMIT 1);
DELETE FROM project_tech WHERE project_id = @prj;
INSERT INTO project_tech (project_id, tech_id)
SELECT @prj, id FROM tech_stack WHERE name IN ('Vue', 'Vuex', 'Vuetify', 'TypeScript', 'Scss', 'Git', 'Jenkins', 'Jira');

UPDATE project SET
    experience_id = @exp_mitmas,
    title = 'KB국민은행 국민지갑 내차등록증 구축',
    summary = '차량 등록 및 검사 관련 화면을 개발하고, 차량 정보 연계와 공통 UI·에러 처리 구조를 구성했습니다.',
    role = '프론트엔드 개발',
    responsibilities = '차량 등록 프로세스와 검사 UI를 공통 컴포넌트로 설계·구현
KB 내부 EAI와 한국교통안전공단 데이터 연계 화면 개발',
    content = '## 수행 업무

- 차량 등록 프로세스와 검사 관련 UI를 공통 컴포넌트로 설계·구현했습니다.
- KB 내부 EAI와 한국교통안전공단(TS) 데이터 연계를 통해 차량 정보를 조회·표시하는 화면과 프론트엔드 로직을 개발했습니다.
- 에러 처리 로직을 공통 모듈로 구성하고 데이터 포매팅을 표준화했습니다.
- 코드 리뷰와 문서화를 통해 공통 컴포넌트 및 모듈의 재사용 기준을 정리했습니다.
- 스프린트 단위로 고객 요구사항을 반영하며 기능을 개발했습니다.
',
    type = 'COMPANY',
    thumbnail_url = NULL,
    is_pinned = 1,
    sort_order = 2,
    start_date = '2023-03-01',
    end_date = '2023-04-30'
WHERE title = 'KB국민은행 국민지갑 내차등록증 구축' AND is_deleted = 0;
INSERT INTO project (experience_id, title, summary, role, responsibilities, content, type, thumbnail_url, is_pinned, sort_order, start_date, end_date, created_at, updated_at)
SELECT @exp_mitmas, 'KB국민은행 국민지갑 내차등록증 구축', '차량 등록 및 검사 관련 화면을 개발하고, 차량 정보 연계와 공통 UI·에러 처리 구조를 구성했습니다.', '프론트엔드 개발', '차량 등록 프로세스와 검사 UI를 공통 컴포넌트로 설계·구현
KB 내부 EAI와 한국교통안전공단 데이터 연계 화면 개발', '## 수행 업무

- 차량 등록 프로세스와 검사 관련 UI를 공통 컴포넌트로 설계·구현했습니다.
- KB 내부 EAI와 한국교통안전공단(TS) 데이터 연계를 통해 차량 정보를 조회·표시하는 화면과 프론트엔드 로직을 개발했습니다.
- 에러 처리 로직을 공통 모듈로 구성하고 데이터 포매팅을 표준화했습니다.
- 코드 리뷰와 문서화를 통해 공통 컴포넌트 및 모듈의 재사용 기준을 정리했습니다.
- 스프린트 단위로 고객 요구사항을 반영하며 기능을 개발했습니다.
', 'COMPANY', NULL, 1, 2, '2023-03-01', '2023-04-30', NOW(6), NOW(6)
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM project WHERE title = 'KB국민은행 국민지갑 내차등록증 구축' AND is_deleted = 0);
SET @prj = (SELECT id FROM project WHERE title = 'KB국민은행 국민지갑 내차등록증 구축' AND is_deleted = 0 ORDER BY id LIMIT 1);
DELETE FROM project_tech WHERE project_id = @prj;
INSERT INTO project_tech (project_id, tech_id)
SELECT @prj, id FROM tech_stack WHERE name IN ('Vue', 'Vuex', 'Vuetify', 'TypeScript', 'Scss', 'Git', 'Jenkins', 'Jira');

UPDATE project SET
    experience_id = @exp_inspot,
    title = 'KB국민은행 렌트온 플랫폼 구축',
    summary = '프로젝트 전 페이지의 퍼블리싱과 UI 개발을 담당했습니다.',
    role = '웹 퍼블리싱 및 UI 개발',
    responsibilities = '페이지별 마크업과 공통 스타일을 구성하고 사용자 인터랙션 구현
입력 데이터 유효성 검사와 화면 애니메이션 구현',
    content = '## 수행 업무

- 페이지별 마크업과 공통 스타일을 구성하고 사용자 인터랙션을 구현했습니다.
- 입력 데이터 유효성 검사와 화면 애니메이션을 구현했습니다.
- 시맨틱 HTML을 적용하고 크로스 브라우징에 대응해 웹표준 기반의 화면을 구축했습니다.
',
    type = 'COMPANY',
    thumbnail_url = NULL,
    is_pinned = 0,
    sort_order = 3,
    start_date = '2022-05-01',
    end_date = '2022-07-31'
WHERE title = 'KB국민은행 렌트온 플랫폼 구축' AND is_deleted = 0;
INSERT INTO project (experience_id, title, summary, role, responsibilities, content, type, thumbnail_url, is_pinned, sort_order, start_date, end_date, created_at, updated_at)
SELECT @exp_inspot, 'KB국민은행 렌트온 플랫폼 구축', '프로젝트 전 페이지의 퍼블리싱과 UI 개발을 담당했습니다.', '웹 퍼블리싱 및 UI 개발', '페이지별 마크업과 공통 스타일을 구성하고 사용자 인터랙션 구현
입력 데이터 유효성 검사와 화면 애니메이션 구현', '## 수행 업무

- 페이지별 마크업과 공통 스타일을 구성하고 사용자 인터랙션을 구현했습니다.
- 입력 데이터 유효성 검사와 화면 애니메이션을 구현했습니다.
- 시맨틱 HTML을 적용하고 크로스 브라우징에 대응해 웹표준 기반의 화면을 구축했습니다.
', 'COMPANY', NULL, 0, 3, '2022-05-01', '2022-07-31', NOW(6), NOW(6)
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM project WHERE title = 'KB국민은행 렌트온 플랫폼 구축' AND is_deleted = 0);
SET @prj = (SELECT id FROM project WHERE title = 'KB국민은행 렌트온 플랫폼 구축' AND is_deleted = 0 ORDER BY id LIMIT 1);
DELETE FROM project_tech WHERE project_id = @prj;
INSERT INTO project_tech (project_id, tech_id)
SELECT @prj, id FROM tech_stack WHERE name IN ('HTML5', 'CSS3', 'Javascript', 'JSP', 'starRoot');

UPDATE project SET
    experience_id = @exp_inspot,
    title = 'KB국민은행 머니크루 플랫폼 구축',
    summary = '마이데이터 기반 고객 자산과 투자성향을 활용하는 금융 플랫폼의 화면 및 사용자 기능을 개발했습니다.',
    role = '프론트엔드 개발',
    responsibilities = '투자 포트폴리오 역경매 시스템의 마크업과 UI·사용자 인터랙션 구현
투자 포트폴리오 PDF 저장, 댓글·대댓글, 자산가 필터 기능 구현',
    content = '## 수행 업무

- 투자 포트폴리오 역경매 시스템의 페이지 마크업과 UI·사용자 인터랙션을 구현했습니다.
- 사용자 정보·자산·투자성향 및 활동 데이터를 Vuex로 관리하고 화면 간 상태를 연계했습니다.
- 투자 포트폴리오 PDF 저장, 댓글·대댓글, 자산가 필터 기능을 구현했습니다.
',
    type = 'COMPANY',
    thumbnail_url = NULL,
    is_pinned = 0,
    sort_order = 4,
    start_date = '2021-03-01',
    end_date = '2021-09-30'
WHERE title = 'KB국민은행 머니크루 플랫폼 구축' AND is_deleted = 0;
INSERT INTO project (experience_id, title, summary, role, responsibilities, content, type, thumbnail_url, is_pinned, sort_order, start_date, end_date, created_at, updated_at)
SELECT @exp_inspot, 'KB국민은행 머니크루 플랫폼 구축', '마이데이터 기반 고객 자산과 투자성향을 활용하는 금융 플랫폼의 화면 및 사용자 기능을 개발했습니다.', '프론트엔드 개발', '투자 포트폴리오 역경매 시스템의 마크업과 UI·사용자 인터랙션 구현
투자 포트폴리오 PDF 저장, 댓글·대댓글, 자산가 필터 기능 구현', '## 수행 업무

- 투자 포트폴리오 역경매 시스템의 페이지 마크업과 UI·사용자 인터랙션을 구현했습니다.
- 사용자 정보·자산·투자성향 및 활동 데이터를 Vuex로 관리하고 화면 간 상태를 연계했습니다.
- 투자 포트폴리오 PDF 저장, 댓글·대댓글, 자산가 필터 기능을 구현했습니다.
', 'COMPANY', NULL, 0, 4, '2021-03-01', '2021-09-30', NOW(6), NOW(6)
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM project WHERE title = 'KB국민은행 머니크루 플랫폼 구축' AND is_deleted = 0);
SET @prj = (SELECT id FROM project WHERE title = 'KB국민은행 머니크루 플랫폼 구축' AND is_deleted = 0 ORDER BY id LIMIT 1);
DELETE FROM project_tech WHERE project_id = @prj;
INSERT INTO project_tech (project_id, tech_id)
SELECT @prj, id FROM tech_stack WHERE name IN ('Vue', 'Vuex', 'Javascript', 'Scss');

UPDATE project SET
    experience_id = @exp_inspot,
    title = '기업 웹사이트 및 금융 플랫폼 구축·운영',
    summary = 'SK넥실리스·현대바이오랜드 등 기업 홈페이지 리뉴얼과 금융 플랫폼 구축에 참여하고, 다수 웹사이트의 운영 및 유지보수를 담당했습니다.',
    role = '프론트엔드 개발 및 웹 퍼블리싱',
    responsibilities = '기업 홈페이지 리뉴얼과 금융 플랫폼 구축 과정에서 UI 구조와 화면 개발
The-K저축은행 웹사이트 웹접근성 개선으로 인증 취득에 기여',
    content = '## 수행 업무

- 기업 홈페이지 리뉴얼과 금융 플랫폼 구축 과정에서 UI 구조 및 화면을 개발했습니다.
- The-K저축은행 웹사이트 운영 과정에서 웹접근성 개선 작업을 수행해 인증 취득에 기여했습니다.
- 한국관광공사 ThisIsMyKorea, 서울공예박물관, 금호건설 웹진, SKC, 국립해양문화재연구소, 성남FC 등의 사이트를 유지보수했습니다.
- 스타일가이드와 공통 스타일을 제작하고, 웹표준·웹접근성 기준을 반영해 화면을 개발·관리했습니다.
',
    type = 'COMPANY',
    thumbnail_url = NULL,
    is_pinned = 0,
    sort_order = 5,
    start_date = '2020-12-01',
    end_date = '2022-08-31'
WHERE title = '기업 웹사이트 및 금융 플랫폼 구축·운영' AND is_deleted = 0;
INSERT INTO project (experience_id, title, summary, role, responsibilities, content, type, thumbnail_url, is_pinned, sort_order, start_date, end_date, created_at, updated_at)
SELECT @exp_inspot, '기업 웹사이트 및 금융 플랫폼 구축·운영', 'SK넥실리스·현대바이오랜드 등 기업 홈페이지 리뉴얼과 금융 플랫폼 구축에 참여하고, 다수 웹사이트의 운영 및 유지보수를 담당했습니다.', '프론트엔드 개발 및 웹 퍼블리싱', '기업 홈페이지 리뉴얼과 금융 플랫폼 구축 과정에서 UI 구조와 화면 개발
The-K저축은행 웹사이트 웹접근성 개선으로 인증 취득에 기여', '## 수행 업무

- 기업 홈페이지 리뉴얼과 금융 플랫폼 구축 과정에서 UI 구조 및 화면을 개발했습니다.
- The-K저축은행 웹사이트 운영 과정에서 웹접근성 개선 작업을 수행해 인증 취득에 기여했습니다.
- 한국관광공사 ThisIsMyKorea, 서울공예박물관, 금호건설 웹진, SKC, 국립해양문화재연구소, 성남FC 등의 사이트를 유지보수했습니다.
- 스타일가이드와 공통 스타일을 제작하고, 웹표준·웹접근성 기준을 반영해 화면을 개발·관리했습니다.
', 'COMPANY', NULL, 0, 5, '2020-12-01', '2022-08-31', NOW(6), NOW(6)
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM project WHERE title = '기업 웹사이트 및 금융 플랫폼 구축·운영' AND is_deleted = 0);
SET @prj = (SELECT id FROM project WHERE title = '기업 웹사이트 및 금융 플랫폼 구축·운영' AND is_deleted = 0 ORDER BY id LIMIT 1);
DELETE FROM project_tech WHERE project_id = @prj;
INSERT INTO project_tech (project_id, tech_id)
SELECT @prj, id FROM tech_stack WHERE name IN ('Vue', 'Vuex', 'Vuetify', 'TypeScript', 'Scss');

UPDATE project SET
    experience_id = NULL,
    title = '개인 포트폴리오 사이트 및 전용 어드민',
    summary = '프로필·포트폴리오·블로그·방명록을 갖춘 공개 사이트와, 내용을 직접 관리하는 어드민·API를 만들었습니다. AI 도구와 협업해 개발하고, 구조 설계와 검증은 직접 수행했습니다.',
    role = '프론트엔드·백엔드 개발',
    responsibilities = 'AI 도구와 협업해 개발하고, 구조 설계와 검증은 직접 수행
경력·프로젝트·블로그를 관리하는 어드민과 JWT 쿠키 인증 구현',
    content = '## 공개 사이트

- Next.js App Router로 프로필·포트폴리오·블로그·방명록 페이지를 구성했습니다.
- 블로그 글은 MDX로 작성하고, 목차·태그·댓글 기능을 구현했습니다.
- 블로그만 검색에 노출하고 프로필은 미리보기를 숨기며, 포트폴리오 상세와 방명록은 색인에서 제외하도록 검색 노출 정책을 설계했습니다.
- robots.txt와 sitemap을 구성해 색인 대상 페이지만 검색엔진에 알리도록 했습니다.

## 어드민

- Ant Design 기반으로 경력·프로젝트·블로그·댓글을 관리하는 어드민을 만들었습니다.
- 비밀번호 로그인 후 JWT를 쿠키로 발급하는 방식으로 관리자 인증을 구현했습니다.
- 공개 사이트와 어드민을 브랜치로 나눠 각각 따로 배포할 수 있는 구조로 구성했습니다.

## API

- Spring Boot와 MySQL로 경력·프로젝트·블로그·댓글 API를 Controller·Service·DTO 구조로 구현했습니다.
- Flyway로 DB 스키마 변경을 버전별 마이그레이션으로 관리했습니다.
- 삭제는 실제로 지우지 않고 숨김 처리해 데이터를 보존하도록 했습니다.
',
    type = 'PERSONAL',
    thumbnail_url = NULL,
    is_pinned = 0,
    sort_order = 6,
    start_date = '2023-06-01',
    end_date = NULL
WHERE title = '개인 포트폴리오 사이트 및 전용 어드민' AND is_deleted = 0;
INSERT INTO project (experience_id, title, summary, role, responsibilities, content, type, thumbnail_url, is_pinned, sort_order, start_date, end_date, created_at, updated_at)
SELECT NULL, '개인 포트폴리오 사이트 및 전용 어드민', '프로필·포트폴리오·블로그·방명록을 갖춘 공개 사이트와, 내용을 직접 관리하는 어드민·API를 만들었습니다. AI 도구와 협업해 개발하고, 구조 설계와 검증은 직접 수행했습니다.', '프론트엔드·백엔드 개발', 'AI 도구와 협업해 개발하고, 구조 설계와 검증은 직접 수행
경력·프로젝트·블로그를 관리하는 어드민과 JWT 쿠키 인증 구현', '## 공개 사이트

- Next.js App Router로 프로필·포트폴리오·블로그·방명록 페이지를 구성했습니다.
- 블로그 글은 MDX로 작성하고, 목차·태그·댓글 기능을 구현했습니다.
- 블로그만 검색에 노출하고 프로필은 미리보기를 숨기며, 포트폴리오 상세와 방명록은 색인에서 제외하도록 검색 노출 정책을 설계했습니다.
- robots.txt와 sitemap을 구성해 색인 대상 페이지만 검색엔진에 알리도록 했습니다.

## 어드민

- Ant Design 기반으로 경력·프로젝트·블로그·댓글을 관리하는 어드민을 만들었습니다.
- 비밀번호 로그인 후 JWT를 쿠키로 발급하는 방식으로 관리자 인증을 구현했습니다.
- 공개 사이트와 어드민을 브랜치로 나눠 각각 따로 배포할 수 있는 구조로 구성했습니다.

## API

- Spring Boot와 MySQL로 경력·프로젝트·블로그·댓글 API를 Controller·Service·DTO 구조로 구현했습니다.
- Flyway로 DB 스키마 변경을 버전별 마이그레이션으로 관리했습니다.
- 삭제는 실제로 지우지 않고 숨김 처리해 데이터를 보존하도록 했습니다.
', 'PERSONAL', NULL, 0, 6, '2023-06-01', NULL, NOW(6), NOW(6)
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM project WHERE title = '개인 포트폴리오 사이트 및 전용 어드민' AND is_deleted = 0);
SET @prj = (SELECT id FROM project WHERE title = '개인 포트폴리오 사이트 및 전용 어드민' AND is_deleted = 0 ORDER BY id LIMIT 1);
DELETE FROM project_tech WHERE project_id = @prj;
INSERT INTO project_tech (project_id, tech_id)
SELECT @prj, id FROM tech_stack WHERE name IN ('Next', 'React', 'TypeScript', 'Scss', 'Ant Design', 'MDX', 'Spring', 'MySQL', 'Flyway');

UPDATE skill_group SET description = '금융 서비스 공통 UI를 설계하고, React·Next.js로 영역을 넓히고 있습니다.', sort_order = 1 WHERE name = 'Front-end' AND is_deleted = 0;
INSERT INTO skill_group (name, description, sort_order)
SELECT 'Front-end', '금융 서비스 공통 UI를 설계하고, React·Next.js로 영역을 넓히고 있습니다.', 1
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM skill_group WHERE name = 'Front-end' AND is_deleted = 0);
SET @grp = (SELECT id FROM skill_group WHERE name = 'Front-end' AND is_deleted = 0 ORDER BY id LIMIT 1);
DELETE FROM skill_group_tech WHERE skill_group_id = @grp;
INSERT INTO skill_group_tech (skill_group_id, sort_order, tech_id)
SELECT @grp, CASE name WHEN 'Vue' THEN 0 WHEN 'Vuetify' THEN 1 WHEN 'TypeScript' THEN 2 WHEN 'Javascript' THEN 3 WHEN 'Scss' THEN 4 WHEN 'React' THEN 5 WHEN 'Next' THEN 6 END, id FROM tech_stack WHERE name IN ('Vue', 'Vuetify', 'TypeScript', 'Javascript', 'Scss', 'React', 'Next');

UPDATE skill_group SET description = 'Spring Boot·MySQL로 API를 구성하며 백엔드로 넓히고 있습니다.', sort_order = 2 WHERE name = 'Server Stack' AND is_deleted = 0;
INSERT INTO skill_group (name, description, sort_order)
SELECT 'Server Stack', 'Spring Boot·MySQL로 API를 구성하며 백엔드로 넓히고 있습니다.', 2
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM skill_group WHERE name = 'Server Stack' AND is_deleted = 0);
SET @grp = (SELECT id FROM skill_group WHERE name = 'Server Stack' AND is_deleted = 0 ORDER BY id LIMIT 1);
DELETE FROM skill_group_tech WHERE skill_group_id = @grp;
INSERT INTO skill_group_tech (skill_group_id, sort_order, tech_id)
SELECT @grp, CASE name WHEN 'Spring' THEN 0 WHEN 'MySQL' THEN 1 END, id FROM tech_stack WHERE name IN ('Spring', 'MySQL');

UPDATE skill_group SET description = 'Git·Jenkins로 협업과 빌드를 하고, 디자인과 배포로 넓히고 있습니다.', sort_order = 3 WHERE name = 'Infra & Tools' AND is_deleted = 0;
INSERT INTO skill_group (name, description, sort_order)
SELECT 'Infra & Tools', 'Git·Jenkins로 협업과 빌드를 하고, 디자인과 배포로 넓히고 있습니다.', 3
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM skill_group WHERE name = 'Infra & Tools' AND is_deleted = 0);
SET @grp = (SELECT id FROM skill_group WHERE name = 'Infra & Tools' AND is_deleted = 0 ORDER BY id LIMIT 1);
DELETE FROM skill_group_tech WHERE skill_group_id = @grp;
INSERT INTO skill_group_tech (skill_group_id, sort_order, tech_id)
SELECT @grp, CASE name WHEN 'AWS' THEN 0 WHEN 'Git' THEN 1 WHEN 'Jenkins' THEN 2 WHEN 'Webpack' THEN 3 WHEN 'Figma' THEN 4 END, id FROM tech_stack WHERE name IN ('AWS', 'Git', 'Jenkins', 'Webpack', 'Figma');

COMMIT;
SET SQL_SAFE_UPDATES = @old_safe_updates;
