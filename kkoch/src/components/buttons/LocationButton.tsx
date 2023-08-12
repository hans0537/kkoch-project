import { useNavigate } from 'react-router-dom';
import './LocationButton.css';

type Props = {
	location: React.ReactNode;
	type: string;
}

const LocationButton = ({location, type } : Props) => {
	const navigate = useNavigate();

	// 양재 지역만 활성화 하기 위한 handler
  const handleCheck = () => {
    if (location === "aT화훼공판장(양재동)") {
			if(type==="admin"){
				navigate("/admin/openSession");
			}else if(type==="user"){
				// 소캣 통신 연결
				const socket = new WebSocket('wss://i9c204.p.ssafy.io/ws/');

				// 웹 소캣 연결
				socket.addEventListener('open', () => {
					console.log("---------------------------------")
					console.log('WebSocket connected'); // 웹소켓 연결 확인 메시지 출력
					socket.send(JSON.stringify({role:"client"}))
				});

				// 서버에 들어가자마자 openVidu 방 코드 받기
				socket.addEventListener("message", (e) => {
					const message = e.data;
					console.log("getmessage", message)
					console.log(message ? true:false)
					// 들어갔을때 관리자가 방을 열었으면 sessionId가 포함된 메세지를 받을것이고
					// Json으로 파싱하여 방 session을 받는다.
					if (message!=="not start auction") {
							// const sessionId = JSON.parse(message).sessionId;
							navigate("/auction/liveSession", {   
							state: {
								sessionId: message,
							}});

							return;
					}  else {
						alert("경매 시작 전입니다.")
						socket.close(); // 소캣 연결 종료
					}

				});
				
		
			}else{
				alert("잘못된 접근입니다.")
			}
    } else {
      alert("서비스 준비중 입니다");
    }
  };

  return (
		<button className="btn mx-6" onClick={handleCheck}>
			<div className="wrapper">
				<p className="text">{location} </p>

				<div className="flower flower1">
					<div className="petal one"></div>
					<div className="petal two"></div>
					<div className="petal three"></div>
					<div className="petal four"></div>
				</div>
				<div className="flower flower2">
					<div className="petal one"></div>
					<div className="petal two"></div>
					<div className="petal three"></div>
					<div className="petal four"></div>
				</div>
				<div className="flower flower3">
					<div className="petal one"></div>
					<div className="petal two"></div>
					<div className="petal three"></div>
					<div className="petal four"></div>
				</div>
				<div className="flower flower4">
					<div className="petal one"></div>
					<div className="petal two"></div>
					<div className="petal three"></div>
					<div className="petal four"></div>
				</div>
				<div className="flower flower5">
					<div className="petal one"></div>
					<div className="petal two"></div>
					<div className="petal three"></div>
					<div className="petal four"></div>
				</div>
				<div className="flower flower6">
					<div className="petal one"></div>
					<div className="petal two"></div>
					<div className="petal three"></div>
					<div className="petal four"></div>
				</div>
			</div>
		</button>

  )
}

export default LocationButton