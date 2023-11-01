import inet.ipaddr.IPAddressString;
import inet.ipaddr.IPAddressStringParameters;

public class MyIPAddress {
    IPAddressStringParameters CIDR_V4 = new IPAddressStringParameters.Builder()
            .allowIPv4(true).allowIPv6(false).allowSingleSegment(false).allowEmpty(false)
            .allowPrefix(true).allowPrefixOnly(false)
            .getIPv4AddressParametersBuilder().allowLeadingZeros(false).getParentBuilder()
            .allow_inet_aton(false)
            .toParams();
    private static final IPAddressStringParameters IPV6_PARAMETERS = new IPAddressStringParameters.Builder()
            .allowIPv4(false).allowIPv6(true).allowSingleSegment(false).allowEmpty(false)
            .allowPrefix(false).allowPrefixOnly(false)
            .getIPv6AddressParametersBuilder().allowLeadingZeros(true).getParentBuilder()
            .allow_inet_aton(false)
            .toParams();
    private static final IPAddressStringParameters IPV6_PARAMETERS_V2 = new IPAddressStringParameters.Builder()
            .allowIPv4(false).allowIPv6(true).allowSingleSegment(false).allowEmpty(false)
            .allowPrefix(false).allowPrefixOnly(false)
            .getIPv6AddressParametersBuilder().allowPrefixLengthLeadingZeros(true).allowUnlimitedLeadingZeros(false)
            .getParentBuilder()
            .allow_inet_aton(false)
            .toParams();

    public static boolean isValidIPv6(String ip) {
        try {
            IPAddressString ipAddressString = new IPAddressString(ip, IPV6_PARAMETERS_V2);
            return ipAddressString.getAddress().isIPv6();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidIPv6CONTROL(String ip) {
        try {
            IPAddressString ipAddressString = new IPAddressString(ip, IPV6_PARAMETERS);
            return ipAddressString.getAddress().isIPv6();
        } catch (Exception e) {
            return false;
        }
    }
}
