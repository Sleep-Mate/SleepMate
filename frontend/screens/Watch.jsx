import React, { useEffect, useRef } from 'react';
import { View, TouchableOpacity, Text, Image } from 'react-native';
import { Camera } from 'expo-camera';
import * as Permissions from 'expo-permissions';
import axios from 'axios';
import tw from 'twrnc';
import { StatusBar } from "expo-status-bar";
import { useNavigation } from '@react-navigation/native';

// 이미지
import watchImg from '../assets/images/watchImg.png';

// 컴포넌트
import CheckAnimation from '../components/Animation/CheckAnimation';

const Watch = () => {

    const navigate = useNavigation();
  

  return (
    <View style={tw`absolute top-0 left-0 right-0 bottom-0 bg-[#11161A]`}>
        <StatusBar style="light" />
        
        <CheckAnimation/>
   
        <Image source={watchImg} resizeMode="contain" style={tw`absolute top-35 left-2 right-0 bottom-0 w-80 h-80 ml-5 z-10`}></Image>
     

      <View style={tw `absolute top-140 left-0 right-0 bottom-0 px-10 z-10 w-full h-100`}>
            <Text style={tw `text-center mb-5 text-white`}>{`갤럭시 워치가 없으신가요?\nSKIP 버튼을 눌러주세요`}</Text>
            <TouchableOpacity onPress={()=>{navigate.navigate("Test")}} style={tw `border-white border-[0.3] rounded-2 h-13 items-center justify-center`}>
                <Text style={tw `text-white text-lg`}>SKIP</Text>
            </TouchableOpacity>
        </View>
    </View>
  );
};

export default Watch;
