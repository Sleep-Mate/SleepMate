import { StatusBar } from "expo-status-bar";
import {Text, View, Dimensions} from 'react-native';
import React, {useState, useEffect} from "react";
import { Video } from "expo-av";
import tw from "twrnc";
import * as Animatable from 'react-native-animatable';

// 컴포넌트
import ClockPicker from "../components/Clock/ClockPicker";
import ConfirmBtn from "../components/Button/ConfirmBtn";


const IntroExplain = () => {

    const [animation1, setAnimation1] = useState(null);
    const [animation2, setAnimation2] = useState(null);
    const [animation3, setAnimation3] = useState(null);
    const [animation4, setAnimation4] = useState(null);
    const [sceen, setSceen] = useState(1);


    const height = Dimensions.get("window").height-150;
    useEffect(() => {
        if (sceen===1 && animation1) {
            setTimeout(() => {
                animation1.fadeOut(2000).then(() => {
                    setAnimation2(animation2Ref => {
                        setSceen(2);
                        if (animation2Ref) {
                            animation2Ref.fadeIn(2000);
                        }
                        return animation2Ref;
                    });
                });
            }, 2000); // 1초 후에 animation1을 사라지게 하고 animation2를 나타나게 함
        }

        if (sceen===2 && animation1) {
            setTimeout(() => {
                animation2.fadeOut(3000).then(() => {
                    setSceen(3);
                    setAnimation3(animation3Ref => {
                        if (animation3Ref) {
                            animation3Ref.fadeIn(3000);
                        }
                        return animation3Ref;
                    });
                });
            }, 3000); // 1초 후에 animation1을 사라지게 하고 animation2를 나타나게 함
        }

        if (sceen===3 && animation1) {
            setTimeout(() => {
                animation3.fadeOut(3000).then(() => {
                    setSceen(4);
                    setAnimation4(animation4Ref => {
                        if (animation4Ref) {
                            animation4Ref.fadeIn(3000);
                        }
                        return animation4Ref;
                    });
                });
            }, 3000); // 1초 후에 animation1을 사라지게 하고 animation2를 나타나게 함
        }
    }, [sceen, animation1]);

    return(
        <View style={tw`flex-1`}>
            
            <StatusBar hidden />

            <Video 
                style={tw`absolute top-0 left-0 right-0 bottom-0`}
                source={require("../assets/videos/walkingLoad.mp4")}
                resizeMode="cover"
                repeat={true}
                shouldPlay={true}
                isLooping={true}
            />
            <View style={tw`absolute top-0 left-0 right-0 bottom-0 bg-black opacity-50`}></View>
            <View style={tw`flex-1`}>
                <Animatable.Text ref={(ref) => setAnimation1(ref)} style={ sceen===1?tw`absolute top-0 left-0 right-0 bottom-0 text-white text-5 mt-[${height}] text-center`:tw`hidden`}>환영합니다.</Animatable.Text>
                <Animatable.Text ref={(ref) => setAnimation2(ref)} style={sceen===2?tw`absolute top-0 left-0 right-0 bottom-0 text-white text-5 mt-[${height}] text-center` :tw`hidden`}>{`저는 여러분의 숙면을 도와드릴\nsleep mate입니다.`}</Animatable.Text>
                <Animatable.Text ref={(ref) => setAnimation3(ref)} style={sceen===3?tw`absolute top-0 left-0 right-0 bottom-0 text-white text-5 mt-[${height}] text-center`:tw`hidden`}>몇 가지 질문을 드리겠습니다.</Animatable.Text>
                
                <Animatable.View ref={(ref) => setAnimation4(ref)} style={sceen===4?tw`absolute top-0 left-0 right-0 bottom-0`:`hidden`}>
                    <ClockPicker display={sceen}/>
                </Animatable.View>
                
                <ConfirmBtn display={sceen}/>
                
                <Animatable.Text ref={(ref) => setAnimation4(ref)} style={sceen===4?tw`absolute top-0 left-0 right-0 bottom-0 text-white text-5 mt-[${height}] text-center`:tw`hidden`}>주로 몇시에 주무시나요?</Animatable.Text>
            
            </View>
        </View>
    )
}

export default IntroExplain;