import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.net.InternetDomainName;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.jsoniter.JsonIterator;
import inet.ipaddr.AddressStringException;
import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressString;
import lombok.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.validator.routines.DomainValidator;

public class Main {
    public static void main(String[] args) {
        try {
            nullEqualityCheck();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static String defang(String value) {
        return StringUtils.trimToEmpty(value).replaceAll("\\.", "\\[\\.\\]");
    }

    public static String refang(String value) {
        return value.replaceAll("\\[\\.\\]", "\\.");
    }

    public static void domainTest() throws MalformedURLException {
        List<String> urlStrings = Arrays.asList(
                "http://www.amazon.com",
                "http://prime.amazon.com",
                "prime.amazon.com",
                "amazon.com"
        );
        for (String u : urlStrings) {
//            InternetDomainName idn = InternetDomainName.from(u);
//            System.out.println(idn);
            System.out.println(DomainValidator.getInstance().isValid(u));
        }
    }

    public static void phoneFormatTest() {
        List<String> phones = Arrays.asList(
                "(555) 555-5555",
                "555-555-5555",
                "5555555555",
                "555/555.5555",
                "555.555.5555",
                "555 555 5555",
                "+5 (555) 5555555",
                "");
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        List<String> curedPhones = phones
                .stream()
//                .map(p -> p.replaceAll("[^0-9]", ""))
                .map(p -> {
                    try {
                        Phonenumber.PhoneNumber pn = phoneNumberUtil.parse(p, "US");
                        phoneNumberUtil.isPossibleNumber(pn);
                        return p;
                    } catch (NumberParseException e) {
                        return "";
                    }
                })
                .collect(Collectors.toList());
        System.out.println(curedPhones);
    }

    public static void parseTest() throws IOException {
//        String testString = "Sarah Perlotto\r\nAge: 99";
//        //(\w+)(\s+)(\w+)
//        Pattern pattern = Pattern.compile("(.+Age: 99)", Pattern.DOTALL);
//        Matcher matcher = pattern.matcher(testString);
//        System.out.println(matcher.matches());
//        Pattern p1 = Pattern.compile("(.+Host: )([\\w\\-\\.:]+)([\\r\\n\\t\r\n\t ]+)(.*)", Pattern.DOTALL);
//        String s1 = "GET /1234567890.functions HTTP/1.1\r\n" +
//                "Accept: */*\r\nAccept-Encoding: gzip, deflate\r\n" +
//                "User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; WOW64; Trident/7.0; .NET4.0C; .NET4.0E)\r\n" +
//                "Host: updatechrome.duckdns.org:81\r\n" +
//                "Connection: Keep-Alive\r\n\r\n";
//        System.out.println(s1);
//        String host;
//        if (s1.contains("Host: ")) {
//            Matcher m1 = p1.matcher(s1);
//            if (m1.matches()) {
//                host = m1.group(2);
//            } else {
//                host = "";
//            }
//        } else {
//            host = "";
//        }
//        System.out.println(host);
    }

    public static void callMeTest() {
        List<Integer> range = IntStream.range(1, 10).boxed().collect(Collectors.toList());
        System.out.println("Answer: " + callMe(range));
    }

    public static boolean callMe(List<Integer> range) {
        for (int i : range) {
            System.out.println(i);
            switch (i) {
                case 2:
                    return true;
                default:
                    System.out.println("Default");
            }
        }
        return false;
    }

    public static void test() {
        String duckDnsDomainSuffix = ".duckdns.org";
        String domain = "cnn.com.duckdns.org";
        if (domain.endsWith(duckDnsDomainSuffix)) {
            //When looking up a subdomain of DuckDNS, only subdomain is needed (e.g. test.duckdns.org >> test)
            domain = domain.replace(duckDnsDomainSuffix, "");
            int idx = domain.indexOf(".");
            if (idx != -1) {
                domain = domain.substring(idx+1);
                System.out.println(domain);
            }
        }
    }
    
    public static void nullPointerTest() {
        Integer i = Integer.valueOf(null);
        System.out.println(i);
        String s = null;
        String t = npe2(s);
        String u = npe1(s);
    }
    
    private static String npe1(String s) {
        return npe2(s);
    }
    
    private static String npe2(String s) {
        return s;
    }
    
    private static void testBooleanWrapper() {
        class HasBoolean {
            private Boolean testMe;
            
            public HasBoolean() { }
            
            public Boolean getTestMe() { return testMe; }
            
            public void setTestMe(Boolean testMe) { this.testMe = testMe; }
        }
        
        HasBoolean testObject = new HasBoolean();
        
        if (Boolean.TRUE.equals(testObject.getTestMe())) {
            System.out.println("Test me exists/is true");
        }
    }
    
    private static void snakeToCamelTest() {
        String camel = ("test_me_a_lot");
        System.out.println(snakeToCamelCase(camel));
    }
    
    private static String snakeToCamelCase(String snakeCase) {
        String camelCase = new StringBuilder().append(snakeCase).toString();
        while (camelCase.contains("_")) {
            camelCase = camelCase.replaceFirst("_[a-z]", String.valueOf(
                    Character.toUpperCase(camelCase.charAt(camelCase.indexOf("_") + 1))));
        }
        return camelCase;
    }
    
    private static void ipv6Test() {
        String i1 = "2001:000db8:3333:4444:5555:6666:7777:8888";
        String i2 = "2001:0db8:3333:4444:5555:6666:7777:8888";

        System.out.println(String.format("IPv6 %s valid? %b", i1, MyIPAddress.isValidIPv6CONTROL(i1)));
        System.out.println(String.format("IPv6 %s valid? %b", i2, MyIPAddress.isValidIPv6CONTROL(i2)));

        System.out.println(String.format("IPv6 %s valid? %b", i1, MyIPAddress.isValidIPv6(i1)));
        System.out.println(String.format("IPv6 %s valid? %b", i2, MyIPAddress.isValidIPv6(i2)));
    }
    
    private static void lombokTest() {
        Child child = Child.builder().id(1).name("Bob").vaccinated(true).build();
        System.out.println(child.toString());
    }
    
    private static void iteratorTest() {
        // Cannot use Arrays.asList due to underlying Array implementation
        List<Integer> integerList = new ArrayList<>();
        integerList.add(1);
        integerList.add(2);
        integerList.add(3);
        integerList.add(4);
        Iterator<Integer> integerIterator = integerList.iterator();
        while (integerIterator.hasNext()) {
            if (integerIterator.next() == 3) {
                integerIterator.remove();
            }
        }
        System.out.println(integerList);
    }
    
    private static void testObjectNullProperty() {
        class TestMe {
            private Boolean isValid;
            
            public TestMe() {}
            
            public TestMe(Boolean isValid) {
                this.isValid = isValid;
            }
            
            public void setIsValid(Boolean isValid) {
                this.isValid = isValid;
            }
            
            public Boolean getIsValid() {
                return isValid;
            }
        }
        
        TestMe t1 = new TestMe(true);
        TestMe t2 = new TestMe(false);
        TestMe t3 = new TestMe();
        
        List<TestMe> testList = Arrays.asList(t1, t2, t3).stream().filter(t -> t.getIsValid()).collect(Collectors.toList());
        System.out.println(testList);
    }
    
    private static void parseRaw() {
        String input = "POCHandle:      DWC53-ARIN\\nIsRole:         N\\nLastName:       Rowland\\nFirstName:      Brad\\nStreet:         902 College Ave.\\nCity:           Brenham\\nState/Prov:     TX\\nCountry:        US\\nPostalCode:     77833\\nRegDate:        1992-06-22\\nUpdated:        2023-01-26\\nOfficePhone:    +1-979-830-4020\\nMailbox:        browland@blinn.edu\\nSource:         ARIN\\n";
        String regex = "POCHandle:\\s+(\\w+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            String captureGroup = matcher.group(1);
            System.out.println(captureGroup);
        }
    }
    
    private static void cidrToIpRange() {
        String targetIp = "64.92.176.12";
        String cidr = "64.92.176.0/21";
        SubnetUtils utils = new SubnetUtils(cidr);
        String[] ips = utils.getInfo().getAllAddresses();
        System.out.println(String.format("First IP: %s", ips[0]));
        System.out.println(String.format("Last IP: %s", ips[ips.length-1]));
        List<String> ipList = Arrays.asList(ips);
        System.out.println(String.format("IPs include %s? %s", targetIp, ipList.contains(targetIp)));
        int count = ips.length;
    }
    private static void nestedNulls() {
        String nullString = null;
        TestObject testObjectNull = null;
        TestObject testObjectBad = new TestObject();
        TestObject testObjectGood = new TestObject();
        testObjectGood.setTestProperty("Test");

        boolean b1 = Objects.isNull(nullString);
        boolean b2 = Objects.isNull(testObjectNull);
//        boolean b3 = Objects.isNull(testObjectNull.getTestProperty());  // NPE
        boolean b4 = Objects.isNull(testObjectBad);
        boolean b5 = Objects.isNull(testObjectBad.getTestProperty());
        boolean b6 = Objects.isNull(testObjectGood);
    }
    
    private static void splitList() {
        List<String> packages = Arrays.asList(
                "PROTECT_ESSENTIALS", 
                "PROTECT_BUSINESS", 
                "INSIGHT_CLASSIC", 
                "INSIGHT", 
                "CONFRONT_ESSENTIALS",
                "",
                null);
        List<String> splitList = packages.stream().map(i -> {
            return StringUtils.isBlank(i) ? "" : i.split("_")[0];
        }).collect(Collectors.toList());
        Set<String> splitSet = splitList.stream().collect(Collectors.toSet());
        boolean valid = splitSet.size() == splitSet.size();
        return;
    }
    
    private static void randomStrings() {
        System.out.println(new StringBuilder(RandomStringUtils.random(9, true, true)).toString());
        System.out.println(new StringBuilder(RandomStringUtils.random(9, true, true)).toString());
        System.out.println(new StringBuilder(RandomStringUtils.random(9, true, true)).toString());
        System.out.println(new StringBuilder(RandomStringUtils.random(9, true, true)).toString());
        System.out.println(new StringBuilder(RandomStringUtils.random(9, true, true)).toString());
    }
    
    private static void onePackagePerProduct() {
        List<String> productList1 = Arrays.asList("INSIGHT", "CONFRONT", "PROTECT");
        List<String> productList2 = Arrays.asList("INSIGHT");

        List<String> packageList1 = Arrays.asList("INSIGHT_ANALYST", "CONFRONT_ANALYST", "PROTECT_ANALYST");
        List<String> packageList2 = Arrays.asList("INSIGHT_ANALYST", "CONFRONT_ANALYST", "PROTECT_ANALYST", "PROTECT_EXECUTIVE");
        List<String> packageList3 = Arrays.asList("INSIGHT_ANALYST", "CONFRONT_ANALYST");
        List<String> packageList4 = Arrays.asList("PROTECT_ANALYST");
        List<String> packageList5 = Arrays.asList("INSIGHT_ANALYST", "PROTECT_ANALYST", "CONFRONT_ANALYST");
        List<String> packageList6 = Arrays.asList("INSIGHT_ANALYST");
        

        List<String> chosenProducts = productList2;
        List<String> chosenPackages = packageList6;
        List<String> packageProductPrefixes = chosenPackages.stream()
                .map(p -> StringUtils.isBlank(p) ? "" : p.split("_")[0])
                .collect(Collectors.toList());
        boolean match = CollectionUtils.isEqualCollection(chosenProducts, packageProductPrefixes);
        System.out.println(String.format("1:1 match between products (%s)/packages (%s)? %b", chosenProducts, chosenPackages, match));

//        for (String product : productList2) {
//            Integer productPackageCount = Collections.frequency(packageProductPrefixes, product);
//            if (productPackageCount != 1) {
//                System.out.println(String.format("More/less than one occurrence of %s in %s", product, packageProductPrefixes));
//            }
//        }
        
//        // Isolate package products
//        List<String> packageProductPrefixes = packageList1.stream()
//                .map(p -> StringUtils.isBlank(p) ? "" : p.split("_")[0])
//                .collect(Collectors.toList());
//        Set<String> testSet = packageProductPrefixes.stream().collect(Collectors.toSet());
    }
    
    private static void testPackageProductValidation() {
        Product insight = Product.builder().id(1).name("INSIGHT").build();
        Product confront = Product.builder().id(2).name("CONFRONT").build();
        Product protect = Product.builder().id(3).name("PROTECT").build();
        List<Product> validProducts = Arrays.asList(insight, confront, protect);

        Package insightSmall = Package.builder().id(1).name("INSIGHT_SMALL").product(insight).build();
        Package insightLarge = Package.builder().id(2).name("INSIGHT_LARGE").product(insight).build();
        Package confrontSmall = Package.builder().id(3).name("CONFRONT_SMALL").product(confront).build();
        Package confrontLarge = Package.builder().id(4).name("CONFRONT_LARGE").product(confront).build();
        Package protectSmall = Package.builder().id(5).name("PROTECT_SMALL").product(protect).build();
        Package protectLarge = Package.builder().id(6).name("PROTECT_LARGE").product(protect).build();
        List<Package> validPackages = Arrays.asList(insightSmall, insightLarge, confrontSmall, confrontLarge, 
                protectSmall, protectLarge);
        
        Set<Product> msspProducts = Arrays.asList(insight, confront)
                .stream().collect(Collectors.toSet());
        Set<Package> msspPackages = Arrays.asList(insightSmall, confrontSmall, confrontLarge)
                .stream().collect(Collectors.toSet());
        
        List<String> requestProducts = Arrays.asList("INSIGHT", "CONFRONT", "PROTECT");
        List<String> requestPackages = Arrays.asList("INSIGHT_SMALL", "CONFRONT_SMALL", "CONFRONT_LARGE", "PROTECT_SMALL");
        
        Request request = Request.builder().products(requestProducts).packages(requestPackages).build();
        validateProductsAndPackagesForOrganization(request, validPackages, validProducts, msspPackages, msspProducts);
        
        System.out.println(request.getProducts().toString());
        System.out.println(request.getPackages().toString());
    }

    private static void validateProductsAndPackagesForMssp(Request request, List<Package> validPackages,
                                             List<Product> validProducts) 
            throws RuntimeException {
        // Products must be valid
        List<String> requestProducts = request.getProducts();
        Set<Product> productSet = requestProducts.stream()
                .map(p -> {
                    try {
                        return validProducts.stream()
                                .filter(ap -> ap.getName().equalsIgnoreCase(p))
                                .findFirst().get();
                    } catch (Exception e) {
                        throw new RuntimeException("Invalid products");
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // Packages must be valid and included under Product
        List<String> requestPackages = request.getPackages();
        Set<Package> packageSet = requestPackages.stream()
                .map(p -> {
                    try {
                        return validPackages.stream()
                                .filter(ap -> ap.getName().equalsIgnoreCase(p))
                                .findFirst().get();
                    } catch (Exception e) {
                        throw new RuntimeException("Invalid packages");
                    }
                })
                .filter(Objects::nonNull)
                .filter(p -> productSet.contains(p.getProduct()))
                .collect(Collectors.toSet());

        // At least one package required per product
        if (!CollectionUtils.isEqualCollection(
                productSet.stream()
                        .map(p -> p.getName())
                        .collect(Collectors.toSet()),
                packageSet.stream()
                        .map(p -> p.getProduct().getName())
                        .collect(Collectors.toSet()))) {
            throw new RuntimeException("Must specify at least one package per product");
        }

        request.setProducts(productSet.stream()
                .map(p -> p.getName())
                .collect(Collectors.toList()));

        request.setPackages(packageSet.stream()
                .map(p -> p.getName())
                .collect(Collectors.toList()));
    }

    private static void validateProductsAndPackagesForOrganization(Request request,
                                             List<Package> validPackages, List<Product> validProducts,
                                             Set<Package> msspPackages, Set<Product> msspProducts) 
            throws RuntimeException {
        // Products must be valid and included under MSSP
        List<String> requestProducts = request.getProducts();
        Set<Product> productSet = requestProducts.stream()
                .map(p -> {
                    try {
                        return validProducts.stream()
                                .filter(ap -> ap.getName().equalsIgnoreCase(p))
                                .findFirst().get();
                    } catch (Exception e) {
                        throw new RuntimeException("Invalid products");
                    }
                })
                .filter(Objects::nonNull)
                .filter(p -> msspProducts.contains(p))
                .collect(Collectors.toSet());

        if (CollectionUtils.isEmpty(productSet)) {
            throw new RuntimeException("No valid products");
        }

        // Packages must be valid, included under Product, and included under MSSP
        List<String> requestPackages = request.getPackages();
        Set<Package> packageSet = requestPackages.stream()
                .map(p -> {
                    try {
                        return validPackages.stream()
                                .filter(ap -> ap.getName().equalsIgnoreCase(p))
                                .findFirst().get();
                    } catch (Exception e) {
                        throw new RuntimeException("Invalid packages");
                    }
                })
                .filter(Objects::nonNull)
                .filter(p -> productSet.contains(p.getProduct()) && msspPackages.contains(p))
                .collect(Collectors.toSet());

        // One package required per product
        if (!CollectionUtils.isEqualCollection(
                productSet.stream()
                        .map(p -> p.getName())
                        .collect(Collectors.toSet()),
                packageSet.stream()
                        .map(p -> p.getProduct().getName())
                        .collect(Collectors.toList()))) {
            throw new RuntimeException("Must specify one package per product");
        }

        request.setProducts(productSet.stream()
                .map(p -> p.getName())
                .collect(Collectors.toList()));

        request.setPackages(packageSet.stream()
                .map(p -> p.getName())
                .collect(Collectors.toList()));
    }
    
    private static void nullEqualityCheck() {
        String nullString = null;
        String notNullString = "not null";
        boolean a = notNullString.equals(nullString);
        boolean b = nullString.equals(notNullString);
    }
}
