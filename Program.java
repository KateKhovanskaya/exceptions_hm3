import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Program {
    
    private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args){
        System.out.println("Введите через пробел фамилию, имя отчество,\n" + 
            "дату рождения в формате dd.mm.yyyy, номер телефона без знака +, без разделителей\n"+
            "и пол буквами f или m");
        String[] userInputArr = createUserInfo();
        // System.out.println(Arrays.toString(userInputArr));
        Map <String, String> userMap = createUserMap();
        userInputArr = fixedBirsday(userInputArr, userMap);
        userInputArr = fixedPhone(userInputArr, userMap);
        userInputArr = fixedGender(userInputArr, userMap);
        fixedFio(userInputArr, userMap);
        String fileName = userMap.get("surname") + ".txt";
        try(FileWriter fw = new FileWriter(fileName, true)){
            fw.write(createResult(userMap));
        }catch(IOException e){
            System.out.println(e.getStackTrace());
        }

    }
    public static String[] createUserInfo(){
        // System.out.println("createUserInfo started");
        String[] userInputArr = new String[6];
        boolean inputNotFinished = true;
        while(inputNotFinished){
            String userInput = scanner.nextLine();
            int countVerification = isRightSize(userInput);
            // System.out.println("результат проверки количества" + countVerification);
            if (countVerification == 1){
                userInputArr = userInput.split(" ");
                inputNotFinished = false;
            }else{
                switch(countVerification)  {
                case -2: System.out.println("Вы ввели недостаточно данных, повторите, пожалуйста, ввод");
                    break;
                case -1: System.out.println("Вы ввели лишние данные, повторите, пожалуйста, ввод");
                break;
                }
            }

        }
        return userInputArr;
    }

    private static Map<String, String> createUserMap(){
        Map <String, String> userInfo = new HashMap<>();
        userInfo.put("surname", "");
        userInfo.put("name", "");
        userInfo.put("patronymic", "");
        userInfo.put("birthday", "");
        userInfo.put("phone", "");
        userInfo.put("gender", "");
        return userInfo;
    } 

    private static int isRightSize(String userInput){
        // System.out.println("isRightSize started");
        String[] userInputArr = userInput.split(" ");
        if(userInputArr.length<6) return -2;
        if(userInputArr.length>6) return -1;
        return 1;
    }

    private static int findDataIndex(String[] arr) throws ParseException{
        // System.out.println("findDataIndex start");
        int index = -1;
        for (int i=0; i<arr.length; i++){
            for (int j = 0; j<arr[i].length(); j++){
                if(!Character.isDigit(arr[i].charAt(j)) && arr[i].charAt(j) != '.'){
                    index=-1;
                    break;
                }
                index = i;
            }
            if (index!=-1) {
                
                checkedDataFormat(arr[i]);
                // System.out.println(index);
                return index; 
            }
        }
        // System.out.println("findDataIndex finished");
        return index;
    }

    private static void checkedDataFormat(String str) throws ParseException{
        // System.out.println("checkedDataFormat start");
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            Date birthday = df.parse(str);
            // System.out.println(df.format(birthday));
    }
    
    private static String[] fixedBirsday(String[] arr, Map<String,String> map){
        boolean birsdayIsFind = false;
        while(!birsdayIsFind){
            try{
                int indexOfBirsday = findDataIndex(arr);
                map.put("birthday", arr[indexOfBirsday]);
                arr[indexOfBirsday] = "";
                birsdayIsFind = true;
            }catch(ParseException e){
                System.out.println("Не найдены данные, соответствующие дате в формате dd.mm.yyyy\n" +
                "повторите, пожалуйста, ввод");
                arr = createUserInfo();
            }catch(ArrayIndexOutOfBoundsException e){
                System.out.println("Не найдены данные, соответствующие дате в формате dd.mm.yyyy\n" +
                "повторите, пожалуйста, ввод");
                arr = createUserInfo();   
            }
        }
        return arr;
    }

    private static int findPhoneIndex(String[] arr){
        // System.out.println("findDataIndex start");
        int index = -1;
        for (int i=0; i<arr.length; i++){
            if (arr[i]!= ""){
                for (int j = 0; j<arr[i].length(); j++){
                    if(!Character.isDigit(arr[i].charAt(j))){
                        index=-1;
                        break;
                    }
                    index = i;
                }
            }
            if (index!=-1) {
                return index; 
            }
        }
        // System.out.println("findDataIndex finished");
        return index;
    }

    private static String[] fixedPhone(String[] arr, Map<String,String> map){
        boolean phoneIsFind = false;
        while(!phoneIsFind){
            try{
                int indexOfPhone = findPhoneIndex(arr);
                map.put("phone", arr[indexOfPhone]);
                arr[indexOfPhone] = "";
                phoneIsFind = true;
            }catch(ArrayIndexOutOfBoundsException e){
                System.out.println("Не найдены данные, соответствующие номеру телефона без + и без разделителей\n" +
                "повторите, пожалуйста, ввод");
                arr = createUserInfo();
                arr = fixedBirsday(arr, map);  
            }
        }
        return arr;
    }

    public static void fixedFio(String[] arr, Map <String, String> map){
        for(int i=0; i<= arr.length; i++){
            if(arr[i]!=""){
                map.put("surname", arr[i]);
                arr[i]="";
                break;
            }
        }
        for(int i=0; i<= arr.length; i++){
            if(arr[i]!=""){
                map.put("name", arr[i]);
                arr[i]="";
                break;
            }
        }
        for(int i=0; i<= arr.length; i++){
            if(arr[i]!=""){
                map.put("patronymic", arr[i]);
                arr[i]="";
                break;
            }
        }
    }

    private static int findGenderIndex(String[] arr){
        int index = -1;
        for (int i=0; i<arr.length; i++){
            
            if(arr[i].equals("m") || arr[i].equals("f")){
                index=i;
                break;
            }
        }
        return index;
    }

    private static String[] fixedGender(String[] arr, Map<String,String> map){
        boolean genderIsFind = false;
        while(!genderIsFind){
            try{
                // System.out.println(Arrays.toString(arr));
                int indexOfGender = findGenderIndex(arr);
                // System.out.println(indexOfGender);
                map.put("gender", arr[indexOfGender]);
                arr[indexOfGender] = "";
                genderIsFind = true;
            }catch(ArrayIndexOutOfBoundsException e){
                System.out.println("Не найдены данные, соответствующие полу\n" +
                "повторите, пожалуйста, ввод");
                arr = createUserInfo();
                arr = fixedBirsday(arr, map);
                arr = fixedPhone(arr, map);
            }
        }
        return arr;
    }

    private static String createResult(Map<String, String> map){
        StringBuilder result = new StringBuilder();
            result.append("<");
            result.append(map.get("surname"));
            result.append(">");
            result.append("<");
            result.append(map.get("name"));
            result.append(">");
            result.append("<");
            result.append(map.get("patronymic"));
            result.append(">");
            result.append("<");
            result.append(map.get("birthday"));
            result.append(">");
            result.append("<");
            result.append(map.get("phone"));
            result.append(">");
            result.append("<");
            result.append(map.get("gender"));
            result.append(">");
            result.append("\n");
        return result.toString();
    }

}
    