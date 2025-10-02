import { useState } from 'react';
import { useNavigate } from 'react-router';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { useAuth } from '@/hooks/useAuth';

function LoginPage() {
    
    const[email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(''); 
    const navigate = useNavigate(); 
    const {login} = useAuth();

    const handleLogin = async (e) => {
        e.preventDefault(); 
        setError(''); 

        try {
            await login(email, password);
            navigate('/');
        } catch (err) {
                console.error('Giriş hatası:', err);
            setError('Giriş başarısız. Lütfen bilgilerinizi kontrol edin.');
        }
    };

    return (
        <div className="flex min-h-svh flex-col items-center 
 justify-center bg-background p-4">
            <div className="w-full max-w-md space-y-6">
                <div className="text-center">
                    <h1 className="text-3xl font-bold text">Giriş Yap</h1>
                    <p className="text-muted-foreground">
                        Hesabınıza erişmek için email ve şifrenizi girin.
                    </p>
                </div>
                <form onSubmit={handleLogin} className="space-y-4">
                    <div className="grid gap-2">
                        <Label htmlFor="email">Email</Label>
                        <Input
                            id="email"
                            type="email"
                            placeholder="ornek@patisoru.com"
                            required
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                        />
                    </div>
                    <div className="grid gap-2">
                        <Label htmlFor="password">Şifre</Label>
                        <Input
                            id="password"
                            type="password"
                            required
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                    </div>
                    {error && <p className="text-sm text-destructive">
                        {error}</p>}
                    <Button type="submit" className="w-full">
                        Giriş Yap
                    </Button>
                </form>
            </div>
        </div>
    );
}

export default LoginPage;