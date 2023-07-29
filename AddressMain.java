import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class AddressMain {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    private static final int RECORDS_PER_ITERATION = 1000;

    private static List<Address> addresses = new ArrayList<>();

    public AddressMain(List<Address> addresses) {
        AddressMain.addresses = addresses;
    }

    // Метод для поиска адресов по переданным идентификаторам на указанную дату
    public static Map<Integer, String> findAddressesByObjectIdsAndDate(Date date, List<Integer> objectIds) {
        // Фильтруем адреса по указанным идентификаторам и актуальности на переданную дату
        List<Address> filteredAddresses = addresses.stream()
                .filter(address -> objectIds.contains(address.getObjectId()) && address.isActualOnDate(date)).toList();

        // Собираем описание адресов в формате "objectId: тип + название"
        Map<Integer, String> result = new HashMap<>();
        for (Address address : filteredAddresses) {
            String description = address.getTypeName() + " " + address.getName();
            result.put(address.getObjectId(), description);
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        // Путь к файлу AS_ADDR_OBJ и AS_ADM_HIERARCHY
        String pathToAddrObjFile = "C:\\Users\\maxim\\Downloads\\test\\AS_ADDR_OBJ.XML";
        File file = new File(pathToAddrObjFile);

        // Загрузка данных из файла
        processAddr(file);
        // Входные параметры
        String inputDateStr = "2010-01-01";
        List<Integer> objectIds = List.of(1422396, 1450759, 1449192, 1451562);

        // Парсинг даты из строки
        Date inputDate = dateFormat.parse(inputDateStr);

        // Поиск адресов
        Map<Integer, String> result = AddressMain.findAddressesByObjectIdsAndDate(inputDate, objectIds);

        // Вывод результатов
        result.forEach((objectId, description) -> System.out.println(objectId + ": " + description));
    }

    public static void processAddr(File file) {
        try (InputStream is = new FileInputStream(file)) {
            XMLAttributeReader xmlReader = new XMLAttributeReader(is, "OBJECT", RECORDS_PER_ITERATION);
            int i = 0;
            int j = 1;
            List<Address> list;
            while (xmlReader.hasNext()) {
                list = xmlReader.getNextPart(Address.class);
                i += Math.min(RECORDS_PER_ITERATION, list.size());
                if ((i / RECORDS_PER_ITERATION) != j) {
                    j = i / RECORDS_PER_ITERATION;
                    System.out.println("saved records: " + i);
                }
                addresses.addAll(list);
                list.clear();
            }
            xmlReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}