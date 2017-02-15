package it.greenvulcano.gvesb.virtual.smtp;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.w3c.dom.Node;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;

import it.greenvulcano.configuration.XMLConfig;
import it.greenvulcano.gvesb.buffer.GVBuffer;
import it.greenvulcano.gvesb.virtual.CallOperation;
import junit.framework.TestCase;

public class SMTPCallOperationTest extends TestCase {

	
	private static final String     TEST_SYSTEM    = "TEST_SYSTEM";
    private static final String     TEST_SERVICE   = "TEST_SERVICE";
    private static final String     TEST_MESSAGE   = "<ns1:headerNews xmlns:ns1=\"http://www.greenvulcano.it/gvesb\"><ns1:autsign>MAK</ns1:autsign><ns1:credate>2006-06-13T09:44:21.500Z</ns1:credate><ns1:takenr>1</ns1:takenr><ns1:version>1</ns1:version><ns1:title>Title</ns1:title><ns1:subtitle>Subtitle</ns1:subtitle><ns1:priority>1</ns1:priority><ns1:keytitle>Keytitle</ns1:keytitle><ns1:cresign>Cresign</ns1:cresign><ns1:categ>Cat</ns1:categ><ns1:subcateg>sub</ns1:subcateg><ns1:subjrefnr1>subj1</ns1:subjrefnr1><ns1:subjrefnr2>subj2</ns1:subjrefnr2><ns1:subjrefnr3>subj3</ns1:subjrefnr3><ns1:intaddr>INTADDR</ns1:intaddr><ns1:intqbx>QBX</ns1:intqbx><ns1:msg>MSG</ns1:msg><ns1:takeid>1</ns1:takeid><ns1:typesign>typeSign</ns1:typesign></ns1:headerNews>";
    private static final String     TEST_MESSAGE_1 = "Test Body";

    private Context                 context;

    private static GreenMail        server       = null;
	
	/**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception
    {
        ServerSetup SMTP = new ServerSetup(11025, null, ServerSetup.PROTOCOL_SMTP);
        ServerSetup POP3 = new ServerSetup(11110, null, ServerSetup.PROTOCOL_POP3);
        server = new GreenMail(new ServerSetup[] {SMTP, POP3});
        server.setUser("test@gv.com", "password");
        server.setUser("test1@gv.com", "password");
        server.setUser("test2@gv.com", "password");
        server.setUser("test3@gv.com", "password");
        server.start();

        context = new InitialContext();
    }
	
	/**
     * Tests email send
     *
     * @throws Exception
     *         if any error occurs
     */
    public final void testSendEmail() throws Exception
    {
        Node node = XMLConfig.getNode("GVCore.xml", "//*[@name='SendEmail']");
        CallOperation op = new SMTPCallOperation();
        op.init(node);

        GVBuffer gvBuffer = new GVBuffer(TEST_SYSTEM, TEST_SERVICE);
        gvBuffer.setObject(TEST_MESSAGE);
        gvBuffer.setProperty("MAIL_SENDER", "SENDER ADDITIONAL INFO");

        op.perform(gvBuffer);
        
        //assertTrue(server.waitForIncomingEmail(5000, 1));

        Message[] messages = server.getReceivedMessages();
        assertEquals(1, messages.length);
        Message email = messages[0];
        Multipart mp = (Multipart) email.getContent();
        assertEquals("Notifica SendEmail", email.getSubject());
        assertEquals(TEST_MESSAGE, GreenMailUtil.getBody(mp.getBodyPart(0)));
        
        System.out.println("---------MAIL DUMP: START");
        System.out.println("Headers:\n" + GreenMailUtil.getHeaders(email));
        System.out.println("---------");
        System.out.println("Body:\n" + GreenMailUtil.getBody(email));
        System.out.println("---------MAIL DUMP: END");
        assertEquals("Notifica SendEmail", email.getHeader("Subject")[0]);
        assertEquals("1", email.getHeader("X-Priority")[0]);
    }
    
    /**
     * Tests email send
     *
     * @throws Exception
     *         if any error occurs
     */
    public final void testSendEmailDynamicDest() throws Exception
    {
        Node node = XMLConfig.getNode("GVCore.xml", "//*[@name='SendEmailDynamicDest']");
        CallOperation op = new SMTPCallOperation();
        op.init(node);

        GVBuffer gvBuffer = new GVBuffer(TEST_SYSTEM, TEST_SERVICE);
        gvBuffer.setObject(TEST_MESSAGE);

        gvBuffer.setProperty("GV_SMTP_TO",  "test1@gv.com");
        gvBuffer.setProperty("GV_SMTP_CC",  "test2@gv.com");
        gvBuffer.setProperty("GV_SMTP_BCC", "test3@gv.com");

        op.perform(gvBuffer);

        Message[] messages = server.getReceivedMessages();
        assertEquals(3, messages.length);
        Message email = messages[0];
        Multipart mp = (Multipart) email.getContent();
        assertEquals("Notifica SendEmailDynamicDest", email.getSubject());
        assertEquals(TEST_MESSAGE_1, GreenMailUtil.getBody(mp.getBodyPart(0)));
        
        System.out.println("---------MAIL DUMP: START");
        System.out.println("Headers:\n" + GreenMailUtil.getHeaders(email));
        System.out.println("---------");
        System.out.println("Body:\n" + GreenMailUtil.getBody(email));
        System.out.println("---------MAIL DUMP: END");
        assertEquals("Notifica SendEmailDynamicDest", email.getHeader("Subject")[0]);
        assertEquals("test1@gv.com", email.getHeader("To")[0]);
        assertEquals("test2@gv.com", email.getHeader("Cc")[0]);
    }
    
    /**
     * Tests email send
     *
     * @throws Exception
     *         if any error occurs
     */
    public final void testSendEmailBufferAttach() throws Exception
    {
        Node node = XMLConfig.getNode("GVCore.xml", "//*[@name='SendEmailBufferAttach']");
        CallOperation op = new SMTPCallOperation();
        op.init(node);

        GVBuffer gvBuffer = new GVBuffer(TEST_SYSTEM, TEST_SERVICE);
        gvBuffer.setObject(TEST_MESSAGE);

        op.perform(gvBuffer);

        Message[] messages = server.getReceivedMessages();
        assertEquals(1, messages.length);
        Message email = messages[0];
        Multipart mp = (Multipart) email.getContent();
        assertEquals("Notifica SendEmailBufferAttach", email.getSubject());
        assertEquals(TEST_MESSAGE_1, GreenMailUtil.getBody(mp.getBodyPart(0)));
        
        System.out.println("---------MAIL DUMP: START");
        System.out.println("Headers:\n" + GreenMailUtil.getHeaders(email));
        System.out.println("---------");
        System.out.println("Body:\n" + GreenMailUtil.getBody(mp.getBodyPart(0)));
        System.out.println("---------MAIL DUMP: END");
        assertEquals("Notifica SendEmailBufferAttach", email.getHeader("Subject")[0]);
        assertEquals("Current Data", mp.getBodyPart(1).getFileName());
    }
    
    /**
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception
    {
        if (context != null) {
            try {
                context.close();
            }
            catch (Exception exc) {
                exc.printStackTrace();
            }
        }
        if (server != null) {
            server.stop();
        }
        super.tearDown();
    }

}