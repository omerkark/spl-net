package bgu.spl.net.api.bidi;
import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.Messages.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;

public class EncoderDecoder implements MessageEncoderDecoder {

    private byte[] bytes = new byte[1 << 10];
    private byte [] opCodeArr = new byte[3];
    private int len = 0;
    private int OpcodeCounter = 0;
    private int Counter = 0;
    private short Opcode;
    private boolean follow = false;
    private int numOfusers;
    private int followcounter = 0;



    @Override
    public Object decodeNextByte(byte nextByte)  {

        if (OpcodeCounter < 2) {
            opCodeArr[OpcodeCounter] = nextByte;
            OpcodeCounter++;
            return null;
        }
        if (OpcodeCounter == 2){
            Opcode = bytesToShort(opCodeArr);
            OpcodeCounter++;
        }

        switch (Opcode) {
            // register
            case 1:
                return parseLogReg(Opcode,nextByte);
            //   Login request (LOGIN)
            case 2:
                return parseLogReg(Opcode,nextByte);
            //Logout request (LOGOUT)
            case 3:
                resetAll();
                return new Logout();
            // Follow / Unfollow request
            case 4:

                if (followcounter < 3) {
                    opCodeArr[followcounter] = nextByte;
                    followcounter++;
                    return null;
                }
                if (followcounter == 3){
                    follow = opCodeArr[0] == 1;
                    byte[] numberOfUsers = new byte[2];
                    numberOfUsers[0] = opCodeArr[1];
                    numberOfUsers[1] = opCodeArr[2];
                    numOfusers = bytesToShort(numberOfUsers);
                }

                if (nextByte == '\0'){
                    pushByte(" ".getBytes()[0]);
                    Counter++;
                    if (Counter != numOfusers)
                        return null;
                }

                if (Counter == numOfusers) {
                    String userNameList = new String(bytes, 0, len, StandardCharsets.UTF_8);
                    len = 0;
                    String[] splited = userNameList.split("\\s+");
                    LinkedList<String> usernameList = new LinkedList<>();
                    for (String name : splited) {
                        usernameList.add(name);
                    }
                    resetAll();
                    return new Follow_UnFollow(follow, numOfusers, usernameList);
                }
                else
                    pushByte(nextByte);
                return null;

            // Post request (POST)
            case 5:
                if (nextByte == '\0') {
                    String post = popString();
                    resetAll();
                    return new Post(post);
                }
                else
                    pushByte(nextByte);
                return null;
            // PM request (PM)
            case 6:
                if (nextByte == '\0' & !follow) {
                    pushByte(" ".getBytes()[0]);
                    follow = true;
                    return null;
                }
                if (nextByte == '\0' & follow) {
                    String[] PM = popString().split("\\s+");
                    String userName = PM[0];
                    String content="";
                    for (int i = 1; i < PM.length ; i++) {
                        content = content+PM[i]+" ";
                    }
                    resetAll();
                    return new PM(userName,content);
                }
                pushByte(nextByte);
                return null;
            // User list request (USERLIST)
            case 7:
                resetAll();
                return  new UserListRequest();
            // Stats request (STAT)
            case 8:
                if (nextByte == '\0'){
                    String userName = popString();
                    resetAll();
                    return new StatsRequest(userName);
                }
                else
                    pushByte(nextByte);
                return null;

        }
        return "hellow world";
    }


    private Object parseLogReg(int opcode,byte nextByte) {

        if (nextByte != '\0') {
            pushByte(nextByte);
            return null;
        } else {
            Counter++;
            if (Counter == 2) {
                String LogReg = popString();
                String[] splited = LogReg.split("\\s+");
                resetAll();
                if (opcode == 1)
                    return new Register(splited[0], splited[1]);
                else
                    return new Login(splited[0], splited[1]);

            }
            pushByte(" ".getBytes()[0]);
            return null;
        }
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
        opCodeArr = new byte[3];
        len = 0;
        OpcodeCounter = 0;
        Counter = 0;
        Opcode = 0;
        follow = false;
        numOfusers = 0;
        followcounter = 0;
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

