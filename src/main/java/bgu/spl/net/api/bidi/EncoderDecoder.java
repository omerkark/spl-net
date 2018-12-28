package bgu.spl.net.api.bidi;

import bgu.spl.net.api.MessageEncoderDecoder;



import bgu.spl.net.api.Messages.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;

public class EncoderDecoder implements MessageEncoderDecoder {

    private byte[] bytes = new byte[1 << 10];
    private byte [] opCodeArr = new byte[2];
    private int len = 0;
    int OpcodeCounter = 0;
    int LoginCounter = 0;
    private short Opcode;
    private int RegisterCounter = 2;


    @Override
    public Object decodeNextByte(byte nextByte) {

        if (OpcodeCounter < 2) {
            opCodeArr[OpcodeCounter] = nextByte;
            OpcodeCounter++;
            return null;
        }

        Opcode = bytesToShort(opCodeArr);

        switch (Opcode) {
            // register
            case 1:
                if(nextByte != '\0') {
                    pushByte(nextByte);
                    return null;
                }
                else {
                    RegisterCounter--;
                    if (RegisterCounter == 0) {
                        String register = popString();
                        return parseRegister(register);
                    }
                    pushByte(" ".getBytes()[0]);
                    return null;
                }
                //   Login request (LOGIN)
            case 2:
                if(nextByte != '\0') {
                    pushByte(nextByte);
                    return null;
                }
                else {
                    LoginCounter--;
                    if (LoginCounter == 0) {
                        String Login = popString();
                        return parseLogin(Login);
                    }
                    pushByte(" ".getBytes()[0]);
                    return null;
                }
                //Logout request (LOGOUT)
            case 3:

                // Follow / Unfollow request
            case 4:

                // Post request (POST)
            case 5:


            // PM request (PM)
            case 6:

                // User list request (USERLIST)
            case 7:

                // Stats request (STAT)
            case 8:

        }
        return "hellow world";
    }


    private Register parseRegister(String s){
        String[] splited = s.split("\\s+");
        resetAll();
        RegisterCounter = 2;
        return new Register(splited[0], splited[1]);
    }

    private Login parseLogin(String s){
        String[] splited = s.split("\\s+");
        resetAll();
        LoginCounter=2;
        return new Login(splited[0], splited[1]);
    }

    private String popString() {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        return result;
    }

    private void resetAll(){
        bytes = new byte[1 << 10];
        len = 0;
        Opcode =0;
    }

    public short bytesToShort(byte[] byteArr) {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }
        bytes[len++] = nextByte;
    }

    @Override
    public byte[] encode(Object message) {
        return new byte[0];
    }



}

