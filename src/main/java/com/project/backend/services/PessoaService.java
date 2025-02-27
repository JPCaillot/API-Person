package com.project.backend.services;

import com.project.backend.domain.dtos.PessoaDTO;
import com.project.backend.domain.validators.PessoaValidator;
import com.project.backend.infrastructure.entities.Endereco;
import com.project.backend.infrastructure.entities.Pessoa;
import com.project.backend.infrastructure.repositories.PessoaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class PessoaService implements IPessoaService {
    private final PessoaRepository pessoaRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public PessoaService(final PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @Override
    @Transactional
    public PessoaDTO createPessoa(PessoaDTO pessoaDTO) {
        if (Objects.isNull(pessoaDTO.getIdPessoa())) {
            String nome = pessoaDTO.getNome();
            PessoaValidator.validateNome(nome);
            validateCPFUniqueness(pessoaDTO);

            log.info("Criando cadastro de pessoa com nome: {}", pessoaDTO.getNome());
            Pessoa pessoa = new Pessoa();

            modelMapper.map(pessoaDTO, pessoa);
            assignEndereco(pessoa);
            pessoaRepository.save(pessoa);
            log.info("Criação de cadastro para {} completa", pessoa.getNome());

            modelMapper.map(pessoa, pessoaDTO);
        } else {
            throw new IllegalArgumentException("idPessoa deve ser nulo para criação de novo cadastro");
        }

        return pessoaDTO;
    }

    @Override
    @Transactional
    public PessoaDTO updatePessoa(PessoaDTO pessoaDTO) {
        String nome = pessoaDTO.getNome();
        PessoaValidator.validateNome(nome);
        validateCPFUniqueness(pessoaDTO);

        Pessoa pessoa = retrievePessoa(pessoaDTO);
        modelMapper.map(pessoaDTO, pessoa);
        assignEndereco(pessoa);

        pessoaRepository.save(pessoa);
        log.info("Atualização de cadastro para {} completa", pessoa.getNome());
        modelMapper.map(pessoa, pessoaDTO);
        return pessoaDTO;
    }

    @Override
    public List<PessoaDTO> listPessoas() {
        List<PessoaDTO> response = new ArrayList<>();

        pessoaRepository.getAllPessoasOrderedByNomeAsc().
                forEach(pessoa -> {
                    log.info("Mapeando cadastro de {}", pessoa.getNome());
                    PessoaDTO pessoaDTO = new PessoaDTO();
                    modelMapper.map(pessoa, pessoaDTO);
                    response.add(pessoaDTO);
                });
        log.info("{} cadastros encontrados", response.size());
        return response;
    }

    @Override
    @Transactional
    public void deletePessoa(Integer idPessoa) {
        pessoaRepository.deleteById(idPessoa);
        log.info("Cadastro de id {} deletado com sucesso", idPessoa);
    }

    @Override
    public Map<String, String> getNomeByCPF(List<String> cpfs) {
        List<Object[]> results = pessoaRepository.findCpfAndNomeByCpfs(cpfs);
        Map<String, String> nomeOfEachCPF = new HashMap<>();

        for (Object[] result : results) {
            String cpf = (String) result[0];
            String nome = (String) result[1];
            nomeOfEachCPF.put(cpf, nome);
        }

        return nomeOfEachCPF;
    }

    private void validateCPFUniqueness(PessoaDTO pessoaDTO) {
        String nome = pessoaDTO.getNome();
        log.info("Validando CPF de {}", nome);
        if (pessoaRepository.existsByCpfAndDifferentId(pessoaDTO.getCpf(), pessoaDTO.getIdPessoa())) {
            throw new IllegalArgumentException("CPF já presente em outro cadastro");
        }
        log.info("Validação dos dados de {} completa!", nome);
    }

    private Pessoa retrievePessoa(PessoaDTO pessoaDTO) {
        Pessoa pessoa;
        if (Objects.nonNull(pessoaDTO.getIdPessoa())) {
            log.info("Atualizando cadastro de pessoa com nome: {}", pessoaDTO.getNome());
            pessoa = pessoaRepository.findById(pessoaDTO.getIdPessoa())
                    .orElseThrow(() -> new ObjectNotFoundException(Pessoa.class.getName(), pessoaDTO.getIdPessoa()));
            log.info("Cadastro encontrado, atualizando...");
        } else {
            throw new IllegalArgumentException("idPessoa não pode ser nulo para atualização de cadastro");
        }
        return pessoa;
    }

    private static void assignEndereco(Pessoa pessoa) {
        Endereco endereco = pessoa.getEndereco();
        if (Objects.nonNull(endereco)) {
            endereco.setPessoa(pessoa);
        }
    }
}
