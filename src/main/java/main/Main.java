package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import spring.AlreadyExistingMemberException;
import spring.ChangePasswordService;
import spring.IdPasswordNotMatchingException;
import spring.MemberInfoPrinter;
import spring.MemberListPrinter;
import spring.MemberNotFoundException;
import spring.MemberRegisterService;
import spring.RegisterRequest;

public class Main {

	private static ApplicationContext ctx = null;
	
	public static void main(String[] args) throws IOException {
		ctx = new GenericXmlApplicationContext("classpath:addCtx.xml");
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	
		while(true) {
			System.out.println("명령어를 입력");
			String command = reader.readLine();
			if(command.equals("exit")) {
				System.out.println("종료됨");
				break;
			}
			if(command.startsWith("new")) {
				processNewCommand(command.split(" "));
			}else if(command.startsWith("change")) {
				processChangeCommand(command.split(" "));
			}else if(command.equals("list")) {
				processListCommand();
			}else if(command.startsWith("info")) {
				processInfoCommand(command.split(" "));
			}else {
				printHelp();
			}
		}
	}

	private static void processChangeCommand(String[] arg) {
		if(arg.length != 4) {
			printHelp();
			return;
		}
		ChangePasswordService change = ctx.getBean("changePwdSvc", ChangePasswordService.class);
		try {
			change.changePassword(arg[1], arg[2], arg[3]);
			System.out.println("암호를 변경했습니다.\n");
		}catch(MemberNotFoundException e) {
			System.out.println("존재하지 않는 이메일입니다.\n");
		}catch(IdPasswordNotMatchingException e) {
			System.out.println("이메일과 암호가 일치하지 않습니다.\n");
		}
		
	}

	private static void processNewCommand(String[] arg) {
		if(arg.length != 5) {
			printHelp();
			return;
		}
		MemberRegisterService reg = ctx.getBean("memberRegSvc", MemberRegisterService.class);
		RegisterRequest req = new RegisterRequest();
		req.setEmail(arg[1]);
		req.setName(arg[2]);
		req.setPassword(arg[3]);
		req.setConfirmPassword(arg[4]);
		
		if(!req.isPasswordEqualToConfirmPassword()) {
			System.out.println("암호와 확인이 일치하지 않는다.");
			return;
		}
		try {
			reg.regist(req);
			System.out.println("등록했습니다.\n");
		}catch(AlreadyExistingMemberException e) {
			System.out.println("이미 존재하는 이메일입니다.\n");
		}
		
	}

	private static void processListCommand() {
		MemberListPrinter list = ctx.getBean("listPrinter", MemberListPrinter.class);
		list.printAll();
		
	}

	private static void processInfoCommand(String[] arg) {
		if(arg.length != 2) {
			printHelp();
			return;
		}
		MemberInfoPrinter info = ctx.getBean("infoPrinter" , MemberInfoPrinter.class);
		info.printMemberInfo(arg[1]);
		
	}

	private static void printHelp() {
		System.out.println();
		System.out.println("잘못된 명령어 입니다. 아래 명령어 사용법을 확인 하세요");
		System.out.println("명령어 사용법 : ");
		System.out.println("new email name pwd pwdcheck");
		System.out.println("change email oldPw newPw");
		System.out.println("info email");
		System.out.println();
		
	}
	
}
