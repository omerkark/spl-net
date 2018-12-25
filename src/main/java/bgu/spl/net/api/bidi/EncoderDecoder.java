package bgu.spl.net.api.bidi;

import bgu.spl.net.api.MessageEncoderDecoder;

import java.util.Arrays;

public class EncoderDecoder implements MessageEncoderDecoder {

    private byte[] bytes = new byte[1 << 10];
    private int len = 0;
    int counter = 0;
    short Opcode = -1;

    @Override
    public Object decodeNextByte(byte nextByte) {


        if(counter < 2){
            pushByte(nextByte);
            counter++;
            return null;
        }
        if (Opcode == -1)
            Opcode = bytesToShort(bytes);

        switch (Opcode){
            // register
            case 1:

                break;
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

    @Override
    public byte[] encode(Object message) {
        return new byte[0];
    }
}
