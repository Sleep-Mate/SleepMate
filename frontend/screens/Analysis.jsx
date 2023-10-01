import {View, ScrollView, Dimensions, StyleSheet, Text, Image, ImageBackground} from "react-native";
import { useState } from "react";
import { Video } from "expo-av";
import { StatusBar } from 'expo-status-bar';
import tw from "twrnc";
import {LinearGradient} from 'expo-linear-gradient';
import { useRecoilState} from 'recoil';
import {motionModalState} from '../recoil/modal/motionModalAtom';

// 이미지
import turnOnTheLight from '../assets/background/turnOnTheLight.jpg';
import sunrise from '../assets/videos/sunrise.mp4';

// 컴포넌트
import CalendarHorizontal from "../components/Calendar/CalendarHorizontal";
import SleepDataArriveAlert from "../components/Alert/SleepDataArriveAlert";
import SleepDataInfo from "../components/Alert/SleepDataInfo";
import SleepMotion from "../components/Motion/SleepMotion";
import SleepVideoAlert from "../components/Alert/SleepVideoAlert";
import HR from "../components/Html/HR";
import SleepMotionPercent from "../components/Alert/SleepMotionPercent";
import MotionChart from "../components/Chart/MotionChart";
import BackDrop from "../components/Modal/BackDrop";
import bg from "../assets/images/report-bg.jpg";
import moonflower from "../assets/videos/moonflowerpink.mp4";
import homevideo2 from "../assets/videos/home_video2.mp4"
const Analysis = () => {
    const width = Dimensions.get("window").width;
    const height = Dimensions.get("window").height-500;

    const [kg, setKg] = useState("");

    const [selectedDate, setSelectedDate] = useState(null);
    const [modalVisible, setModalVisible] = useRecoilState(motionModalState);
    return(
        <View style={tw`flex-1 bg-white w-full h-full`}>
            {/* <Image style={tw`absolute top-0 left-0 right-0 bottom-0 w-full h-100`} source={turnOnTheLight}/> */}
            <Video style={tw`absolute top-0 left-0 right-0 bottom-0 w-full h-full`} source={homevideo2} resizeMode={"cover"} repeat={true} paused={false} onAnimatedValueUpdate={() => {}}></Video>
            {/* <Image style={tw `flex-1 absolute top-0 left-0 right-0 bottom-0 w-100 h-70`} source={turnOnTheLight} resizeMode="cover" ></Image> */}
            <ScrollView>
                <View style={tw`rounded-5 shadow-2xl w-full self-center mt-25 px-3 bg-[#000]/60`}>
                    <View style={styles.container}>
                        <CalendarHorizontal onSelectDate={setSelectedDate} selected={selectedDate} />
                        <StatusBar style="auto" />
                    </View>
                    {/* 도착 데이터 */}
                    <SleepDataArriveAlert/>

                    {/* 수면 시간 정리 */}
                    <SleepDataInfo/>

                    <HR/>

                    {/* 수면 자세 */}
                    {/* <SleepVideoAlert/> */}
                    <SleepMotion/>
                    {/* <Text style={tw`text-center font-bold mt-6`}>각 이미지를  Click하면 자세한 나의 모습을 볼 수 있어요!</Text> */}

                    <HR/>

                    {/* 수면 그래프 */}
                    <SleepMotionPercent/>
                    <MotionChart/>
                    <Text style={tw`text-center font-bold mt-5`}>각 데이터를 Click하면 유형별 이미지를 볼 수 있어요!</Text>

                    {modalVisible&&<BackDrop/>}
                    </View>
            </ScrollView>


        </View>

    )
}

export default Analysis

const styles = StyleSheet.create({
    container: {
      alignItems: 'center',
      justifyContent: 'center',
      paddingTop:30,
    },
  });