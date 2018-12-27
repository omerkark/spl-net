package bgu.spl.net.api.bidi;

import bgu.spl.net.api.MessageEncoderDecoder;



import bgu.spl.net.api.Messages.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;

public class EncoderDecoder implements MessageEncoderDecoder {

    private byte[] bytes = new byte[1 << 10];
    private int len = 0;
    private short Opcode;
    private int offSet = 2;
    private int counterFollow = 1;
    private LinkedList<String> followList = new LinkedList();
    private String post = "";


    @Override
    public Object decodeNextByte(byte nextByte) {


        if (len < 2) {
            pushByte(nextByte);
            len++;
            return null;
        }
        if (Opcode == -1)
            Opcode = bytesToShort(bytes);

        switch (Opcode) {

            // register
            case 1:

                if (registerLogin(nextByte) == null)
                    return null;
                else {
                    Register register = new Register(registerLogin(nextByte)[0], registerLogin(nextByte)[1]);
                    resetAll();
                    return register;
                }

                //   Login request (LOGIN)
            case 2:
                if (registerLogin(nextByte) == null)
                    return null;
                else {
                    Login login = new Login(registerLogin(nextByte)[0], registerLogin(nextByte)[1]);
                    resetAll();
                    return login;
                }

                //Logout request (LOGOUT)
            case 3:
                Logout logout = new Logout();
                resetAll();
                return logout;

            // Follow / Unfollow request
            case 4:
                if (len >= 5) {
                    if (offSet == -1)
                        offSet = 5;

                    byte[] numOfusers = new byte[2];
                    numOfusers[0] = bytes[3];
                    numOfusers[1] = bytes[4];
                    int numberOfusers = bytesToShort(numOfusers);
                    if (nextByte == 0 & counterFollow <= numberOfusers) {
                        followList.add(popString(offSet, len - 1));
                        offSet = len + 1;
                        len++;
                        counterFollow++;
                        return null;
                    }
                    if (nextByte == 0) {
                        Follow_UnFollow follow = new Follow_UnFollow(bytes[2] == 1, numberOfusers, followList);
                        resetAll();
                        return follow;
                    }

                    pushByte(nextByte);
                    len++;
                    return null;
                }
                pushByte(nextByte);
                len++;
                return null;

            // Post request (POST)
            case 5:
                if (nextByte == 20){
                    post = post+" "+popString(offSet, len);
                    offSet = len+1;
                    len++;
                    return null;
                }
                if (nextByte == 0){
                    String Post = post;
                    resetAll();
                    return new Post(Post);
                }
                pushByte(nextByte);
                len++;
                return null;

            // PM request (PM)
            case 6:
                if (nextByte == 0 & offSet == 2) {
                    followList.add(0,popString(offSet,len-1));
                    offSet = len+1;
                    len++;
                    return null;
                }
                if (nextByte == 20 & offSet != 2) {
                    post = post + " " + popString(offSet, len - 1);
                    offSet = len+1;
                    len++;
                    return null;
                }
                if (nextByte == 0 & offSet != 2) {
                    PM pm = new PM(followList.get(0), post);
                    resetAll();
                    return pm;
                }
                pushByte(nextByte);
                len++;
                return null;

            // User list request (USERLIST)
            case 7:
                UserListRequest listRequest = new UserListRequest();
                resetAll();
                return listRequest;

            // Stats request (STAT)
            case 8:
                if (nextByte == 0) {
                    StatsRequest statsRequest = new StatsRequest(popString(offSet, len - 1));
                    resetAll();
                    return statsRequest;
                }
                pushByte(nextByte);
                len++;
                return null;
        }

        return "hellow world";
    }

    private String popString(int offset,int len){
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        String result = new String(bytes, offset, len, StandardCharsets.UTF_8);
        return result;
    }

    private void resetAll(){
        bytes = new byte[1 << 10];
        len = 0;
        offSet = 2;
        counterFollow = 1;
        followList = new LinkedList<>();
        post = "";
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

    private String[] registerLogin(Byte nextByte){
        String[] nameNpassword = new String[2];
        if (nextByte == 00 & offSet == 2) {
            offSet = len;
            len++;
            return null;
        }
        if (nextByte == 00 & offSet != -1) {
            nameNpassword[0] = popString(2, offSet -1);
            nameNpassword[1] = popString(offSet + 1, len);
            return nameNpassword;
        }
        else
            pushByte(nextByte);
        len++;
        return null;
    }

}

