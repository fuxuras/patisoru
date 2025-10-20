import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useState } from "react";
import apiClient from "@/lib/api";
import { login } from "@/store/auth";
import { Toaster, toast } from 'sonner';

export function AuthForm({ mode }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [fullName, setFullName] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const isRegisterMode = mode === 'register';

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    const loadingToastId = toast.loading(isRegisterMode ? 'Hesabınız oluşturuluyor...' : 'Giriş yapılıyor...');

    try {
      if (isRegisterMode) {
        await apiClient.post('/auth/register', { fullName, email, password });
        toast.success('Hesap başarıyla oluşturuldu!', {
          description: 'Lütfen hesabınızı doğrulamak için e-postanızı kontrol edin.',
          id: loadingToastId,
        });
      } else {
        const response = await apiClient.post('/auth/login', { email, password });
        const { accessToken } = response.data;
        login(accessToken);
        toast.success('Giriş başarılı!', { id: loadingToastId });
        window.location.href = '/';
      }
    } catch (error) {
      toast.error(error.response?.data?.message || error.message || 'Bilinmeyen bir hata oluştu.', {
        id: loadingToastId,
      });
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <>
      <Toaster richColors position="top-center" />
      <Card className="w-[350px]">
        <CardHeader>
          <CardTitle>{isRegisterMode ? 'Hesap Oluştur' : 'Giriş Yap'}</CardTitle>
          <CardDescription>
            {isRegisterMode
              ? "Hesabınızı oluşturmak için bilgilerinizi girin."
              : "Hesabınıza erişmek için kimlik bilgilerinizi girin."}
          </CardDescription>
        </CardHeader>
        <form onSubmit={handleSubmit}>
          <CardContent>
            <div className="grid w-full items-center gap-4">
              {isRegisterMode && (
                <div className="flex flex-col space-y-1.5">
                  <Label htmlFor="fullName">Tam Adınız</Label>
                  <Input
                    id="fullName"
                    placeholder="Adınız Soyadınız"
                    value={fullName}
                    onChange={(e) => setFullName(e.target.value)}
                    required
                    disabled={isLoading}
                  />
                </div>
              )}
              <div className="flex flex-col space-y-1.5">
                <Label htmlFor="email">Email</Label>
                <Input
                  id="email"
                  type="email"
                  placeholder="ornek@email.com"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                  disabled={isLoading}
                />
              </div>
              <div className="flex flex-col space-y-1.5">
                <Label htmlFor="password">Şifre</Label>
                <Input
                  id="password"
                  type="password"
                  placeholder="Şifreniz"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                  disabled={isLoading}
                />
              </div>
            </div>
          </CardContent>
          <CardFooter>
            <Button type="submit" className="w-full" disabled={isLoading}>
              {isLoading ? 'İşleniyor...' : (isRegisterMode ? 'Kayıt Ol' : 'Giriş Yap')}
            </Button>
          </CardFooter>
        </form>
      </Card>
    </>
  );
}