package com.example.smartparkpj.controller;

import com.example.smartparkpj.dto.*;
import com.example.smartparkpj.service.AdminService;
import com.example.smartparkpj.service.EnterService;
import com.example.smartparkpj.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
@RequestMapping("/admin")
@Log4j2
@RequiredArgsConstructor
public class AdminController {
    @Value("${com.example.upload.tempPath}")
    private String uploadTempPath;
    @Value("${com.example.upload.AttractionPath}")
    private String uploadAttractionPath;
    @Value("${com.example.upload.ShopPath}")
    private String uploadShopPath;
    @Value("${com.example.upload.ConveniencePath}")
    private String uploadConveniencePath;


    final private AdminService adminService;

    final private TicketService ticketService;


    @GetMapping("")
    public String adminMain(){return "redirect:/admin/enter/admin_add_map";}

    @GetMapping("/enter/admin_add_map")
    public void getAdmin_add_map() {
        log.info("GetMapping /enter/admin_add_map ...");
    }

    @GetMapping("/enter/admin_edit_map")
    public void getAdmin_edit_map() {
        log.info("GetMapping /enter/admin_edit_map ...");
    }

    @PostMapping("/enter/addAttraction")
    public String addAttraction(AttractionDTO attractionDTO, MarkerDTO markerDTO){

        log.info("addAttraction---------------- : " + attractionDTO + markerDTO);
        int ano = adminService.addAttraction(attractionDTO,markerDTO);

        for (String imgName : attractionDTO.getFileNames()){
            moveFile(imgName,markerDTO.getType(),"A" + ano);
            moveFile("s_" + imgName,markerDTO.getType(),"A" + ano);
        }
        return "redirect:/admin/enter/admin_add_map";

    }

    @PostMapping("/enter/addShop")
    public String addShop(ShopDTO shopDTO, MarkerDTO markerDTO){

        log.info("addShop---------------- : " + shopDTO + markerDTO);
        int sno = adminService.addShop(shopDTO,markerDTO);

        for (String imgName : shopDTO.getFileNames()){
            moveFile(imgName,markerDTO.getType(),"S" + sno);
            moveFile("s_" + imgName,markerDTO.getType(),"S" + sno);
        }
        return "redirect:/admin/enter/admin_add_map";

    }

    @PostMapping("/enter/addConvenience")
    public String addConvenience(ConvenienceDTO convenienceDTO, MarkerDTO markerDTO){

        log.info("addCon------------------: " + convenienceDTO + markerDTO);
        int cno = adminService.addConvenience(convenienceDTO, markerDTO);
        moveFile(convenienceDTO.getCon_img(),markerDTO.getType(),"C" + cno);
        moveFile("s_" + convenienceDTO.getCon_img(),markerDTO.getType(),"C" + cno);
        return "redirect:/admin/enter/admin_add_map";
    }

    @PostMapping("/enter/editAttraction")
    public String editAttraction(AttractionDTO attractionDTO, MarkerDTO markerDTO){

        log.info("editAttraction------------------: " + attractionDTO + markerDTO);
        adminService.editAttraction(attractionDTO, markerDTO);
        for (String imgName : attractionDTO.getFileNames()){
            moveFile(imgName,markerDTO.getType(),"A" + attractionDTO.getAno());
            moveFile("s_" + imgName,markerDTO.getType(),"A" + attractionDTO.getAno());
        }

        return "redirect:/admin/enter/admin_edit_map";

    }

    private void moveFile(String imgName,String type, String folderName){
        log.info("moveFile: " + imgName);
        try{
            //임시 경로
            FileInputStream fileInputStream = new FileInputStream(uploadTempPath + File.separator + imgName);

            // 저장될 경로 설정
            String resultPath = "";
            if(type.equals("어트랙션")) {
                resultPath = uploadAttractionPath + File.separator + folderName;
            }
            else if(type.equals("매장")){
                resultPath = uploadShopPath + File.separator + folderName;
            }
            else if(type.equals("편의시설")){
                resultPath = uploadConveniencePath + File.separator + folderName;
            }
            File resultFolder = new File(resultPath);
            if(!resultFolder.exists()){
                resultFolder.mkdirs();
            }
            else{
                clearFolder(resultFolder);
            }

            FileOutputStream fileOutputStream = new FileOutputStream(resultPath + File.separator + imgName);


            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

            int i;
            while ((i = bufferedInputStream.read()) != -1) {
                bufferedOutputStream.write(i);
            }
            bufferedInputStream.close();
            bufferedOutputStream.close();
            fileInputStream.close();
            fileOutputStream.close();

            Files.delete(Paths.get(uploadTempPath + File.separator + imgName));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void clearFolder(File folder) { // 폴더 비우기
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    clearFolder(file);
                } else {
                    file.delete();
                }
            }
        }
    }

    @GetMapping("/ticket/adminTicket")
    public void managerListGet(Model model){
        log.info("managerList GET");
//
//        if(bindingResult.hasErrors()){
//            ticketDTO = ticketDTO.builder().build();
//        }
        model.addAttribute("ticketDTO", ticketService.getAll());
    }

    @GetMapping("/remove")
    public String managerListPost(int tno) {
        log.info("managerList POST");
        ticketService.remove(tno);
        return "redirect:/admin/ticket/adminTicket"; // 티켓 리스트 페이지로 리다이렉트
    }

    @GetMapping("/ticket/adminAddTicket")
    public void adminAddTicketGET(){
        log.info("adminAddTicketGE...!!!!");
    }

    @PostMapping("/ticket/adminAddTicket")
    public String adminAddTicket(@Valid TicketDTO ticketDTO,
                                 BindingResult bindingResult, HttpServletRequest httpServletRequest,
                                 RedirectAttributes redirectAttributes){
        log.info(ticketDTO);

        // 오류가 있을 경우 처리
        if(bindingResult.hasErrors()){
            // 예: redirectAttributes.addFlashAttribute("error", "주문 정보가 올바르지 않습니다.");
            return "redirect:/ticket/add"; // 다시 입력 폼으로 리다이렉트
        }

        // 주문 정보를 서비스에 전달
        ticketService.add(ticketDTO);

        return "redirect:/admin/ticket/adminTicket";
    }

}