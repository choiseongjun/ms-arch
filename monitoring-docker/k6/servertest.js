import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
  scenarios: {
    constant_request_rate: {
      executor: 'constant-arrival-rate',
      rate: 200,             // 초당 200개의 요청으로 증가
      timeUnit: '1s',        // 1초 단위로
      duration: '2m',        // 2분 동안 테스트
      preAllocatedVUs: 200,  // 미리 할당할 가상 사용자 수 증가
      maxVUs: 500,           // 최대 가상 사용자 수를 500으로 증가
    },
  },
  thresholds: {
    http_req_duration: ['p(95)<10000'], // 95%의 요청이 10초 이내에 완료되어야 함
    http_req_failed: ['rate<0.3'],      // 실패율 30% 미만
  },
};

export default function () {
  const response = http.get('http://host.docker.internal:8080/test');
  
  // 응답 상태 확인
  if (response.status !== 200) {
    console.log(`Error: ${response.status} - ${response.body}`);
  }
  
  sleep(0.01); // 각 요청 사이의 대기 시간을 0.01초로 최소화
}
