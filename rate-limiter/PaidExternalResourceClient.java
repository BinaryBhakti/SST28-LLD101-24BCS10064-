class PaidExternalResourceClient implements ExternalResourceClient {
    @Override
    public String call(String key) {
        return "External resource used for " + key;
    }
}
