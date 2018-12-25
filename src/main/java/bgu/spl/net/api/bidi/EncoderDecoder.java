package bgu.spl.net.api.bidi;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.Messages.Message;
import bgu.spl.net.api.Messages.Register;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class EncoderDecoder implements MessageEncoderDecoder {

    private byte[] bytes = new byte[1 << 10];
    private int len = 0;
    private short Opcode;
    private int registerLen = -1;

    @Override
    public Object decodeNextByte(byte nextByte) {


        if(len < 2){
            pushByte(nextByte);
            if (len == 2)
                Opcode = bytesToShort(bytes);
            return null;
        }
        switch (Opcode){
            // register
            case 1:
                Register register;
                if (nextByte == 00 & registerLen == -1) {
                    registerLen = len;
                    return null;
                }
                if (nextByte == 00 & registerLen != -1) {
                    register = new Register(popString(2, registerLen), popString(registerLen + 1, len));
                    return register;
                }
                else
                    pushByte(nextByte);
                    return null;

            //   Login request (LOGIN)
            case 2:
                break;
            //Logout request (LOGOUT)
            case 3:

                break;
            // Follow / Unfollow request
            case 4:

                break;
            // Post request (POST)
            case 5:

                break;

            // PM request (PM)
            case 6:

                break;

            // User list request (USERLIST)
            case 7:

                break;
            // Stats request (STAT)
            case 8:
                break;


        }
        return null;
    }

    public short bytesToShort(byte[] byteArr)
    {
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

    private String popString(int offset, int len) {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        String result = new String(bytes, offset, len, StandardCharsets.UTF_8);

        return result;
    }

    @Override
    public byte[] encode(Object message) {
        return new byte[0];
    }
}
