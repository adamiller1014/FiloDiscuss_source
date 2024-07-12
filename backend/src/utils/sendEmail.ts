import nodemailer from "nodemailer";
import { Address } from "nodemailer/lib/mailer";

export async function sendEmail(to: string | Address, html: string | Buffer) {

    // const testAccount = await nodemailer.createTestAccount();
    // console.log('testAccout', testAccount);
    
    const transporter = nodemailer.createTransport({
      host: "smtp.ethereal.email",
      port: 587,
      secure: false,
      auth: {
        user: 'btl3xzpxaze3ievo@ethereal.email', //testAccount.user,
        pass: 'q227PGWXrPRrjCMAmJ' //testAccount.pass,
      },
      tls: {
        rejectUnauthorized: false
      }
    });
    
  const info = await transporter.sendMail({
    from: '"Maddison Foo Koch 👻" <maddison53@ethereal.email>',
    to, 
    subject: "Change password", 
    html,
  });

  console.log("Message sent: %s", info.messageId);
  console.log("Preview URL: %s", nodemailer.getTestMessageUrl(info));
}

